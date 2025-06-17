package com.example.tqt_quiz.presentation.adapters;

import android.content.Context;
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
    private Context context;
    private List<Member> memberList;

    public MemberAdapter(@NonNull Context context, int resource, @NonNull List<Member> memberList) {
        super(context, resource, memberList);
        this.context = context;
        this.memberList = memberList;
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
        TextView tvFirstName;
        TextView tvLastMiddleName;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_member, parent, false);
            holder = new ViewHolder();
            holder.imgAvatar = convertView.findViewById(R.id.img_Avatar_MemberItem);
            holder.tvFirstName = convertView.findViewById(R.id.tv_FirstName_MemberItem);
            holder.tvLastMiddleName = convertView.findViewById(R.id.tv_LastMiddleName_MemberItem);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Member member = memberList.get(position);
        StaticClass.setImage(holder.imgAvatar, member.getAvatar(), R.drawable.resource_default);
        holder.tvFirstName.setText(member.getFirstName());
        holder.tvLastMiddleName.setText(member.getLastMiddleName());

        return convertView;
    }
}
