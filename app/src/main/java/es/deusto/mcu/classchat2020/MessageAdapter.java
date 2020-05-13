package es.deusto.mcu.classchat2020;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder>{
    private List<ChatMessage> messages;

    public MessageAdapter(List<ChatMessage> messages) {
        this.messages = messages;
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.message_layout, parent,false);
        return new MessageViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final MessageViewHolder holder, int position) {
        ChatMessage chatMessage = messages.get(position);
        holder.tvSenderName.setText(chatMessage.getSenderName());
        holder.tvMessageText.setText(chatMessage.getMessageText());
        if (chatMessage.getSenderAvatarURL() != null){
            Glide.with(holder.civAvatar.getContext())
                    .load(chatMessage.getSenderAvatarURL())
                    .into(holder.civAvatar);
        } else {
            holder.civAvatar.setImageResource(android.R.drawable.ic_menu_help);
        }
        String messageURL = chatMessage.getMessageImageURL();
        if (messageURL != null){
            if (messageURL.startsWith("gs://") ||
                    messageURL.startsWith("https://firebasestorage.googleapis.com/"))
            {
                StorageReference storageRef = FirebaseStorage.getInstance()
                        .getReferenceFromUrl(messageURL);
                storageRef.getDownloadUrl().addOnCompleteListener(
                        new OnCompleteListener<Uri>() {
                            @Override
                            public void onComplete(@NonNull Task<Uri> task) {
                                if (task.isSuccessful()) {
                                    String downloadUrl = task.getResult().toString();
                                    Glide.with(holder.ivMessageImage.getContext())
                                            .load(downloadUrl)
                                            .into(holder.ivMessageImage);
                                    holder.ivMessageImage.setVisibility(View.VISIBLE);
                                } else {
                                    holder.ivMessageImage.setVisibility(View.GONE);
                                }
                            }
                        });
            }
        } else {
            holder.ivMessageImage.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public static class MessageViewHolder extends RecyclerView.ViewHolder {
        private TextView tvSenderName;
        private TextView tvMessageText;
        private CircleImageView civAvatar;
        private ImageView ivMessageImage;


        public MessageViewHolder(View itemView) {
            super(itemView);
            tvSenderName = itemView.findViewById(R.id.tv_sender_name);
            tvMessageText = itemView.findViewById(R.id.tv_message_text);
            civAvatar = itemView.findViewById(R.id.civ_avatar);
            ivMessageImage = itemView.findViewById(R.id.iv_message_image);
        }
    }
}
