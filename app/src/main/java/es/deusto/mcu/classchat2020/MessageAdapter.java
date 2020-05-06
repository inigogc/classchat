package es.deusto.mcu.classchat2020;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

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
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
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
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public static class MessageViewHolder extends RecyclerView.ViewHolder {
        private TextView tvSenderName;
        private TextView tvMessageText;
        private CircleImageView civAvatar;

        public MessageViewHolder(View itemView) {
            super(itemView);
            tvSenderName = itemView.findViewById(R.id.tv_sender_name);
            tvMessageText = itemView.findViewById(R.id.tv_message_text);
            civAvatar = itemView.findViewById(R.id.civ_avatar);
        }
    }
}
