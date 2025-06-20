package com.example.tqt_quiz.presentation.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.tqt_quiz.R;
import com.example.tqt_quiz.presentation.classes.Member;
import com.example.tqt_quiz.staticclass.StaticClass;

import java.util.List;

public class MemberAdapter extends ArrayAdapter<Member> {

    public static final int MODE_MEMBER = 0;
    public static final int MODE_PENDING = 1;

    private Context context;
    private List<Member> memberList;
    private int mode;

    public MemberAdapter(@NonNull Context context, int resource, @NonNull List<Member> memberList, int mode) {
        super(context, resource, memberList);
        this.context = context;
        this.memberList = memberList;
        this.mode = mode;
    }

    @Override
    public int getCount() {
        return memberList.size();
    }

    @Override
    public Member getItem(int position) {
        return memberList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    static class ViewHolder {
        ImageView imgAvatar;
        TextView tvName;
        ImageView btnAccept, btnReject;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        Member member = memberList.get(position);
        Log.d("Pending", "Null");

        if (convertView == null) {
            if (mode == MODE_MEMBER) {
                convertView = LayoutInflater.from(context).inflate(R.layout.item_member, parent, false);
            } else {
                convertView = LayoutInflater.from(context).inflate(R.layout.item_pending_member, parent, false);
            }

            holder = new ViewHolder();
            holder.imgAvatar = convertView.findViewById(R.id.img_Avatar_MemberItem);
            holder.tvName = convertView.findViewById(R.id.tv_Name_MemberItem);

            if (mode == MODE_PENDING){
                holder.btnAccept = convertView.findViewById(R.id.btn_Accept_PendingMember);
                holder.btnReject = convertView.findViewById(R.id.btn_Reject_PendingMember);
            }

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        StaticClass.setImage(holder.imgAvatar, member.getAvatar(), R.drawable.resource_default);
        holder.tvName.setText(member.getName());

        if (mode == MODE_PENDING) {
            holder.btnAccept.setOnClickListener(v -> {
                // TODO: xử lý chấp nhận
            });

            holder.btnReject.setOnClickListener(v -> {
                // TODO: xử lý từ chối
            });
        }

        return convertView;
    }
}
