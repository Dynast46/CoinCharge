package com.sundaymorning.coincharge.adapter;

import android.content.Context;
import android.graphics.Color;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.sundaymorning.coincharge.R;
import com.sundaymorning.coincharge.object.HelpNoticeEntry;

import java.util.ArrayList;

/**
 * Created by sweet on 2017-07-08.
 */

public class HelpExpandableAdapter extends BaseExpandableListAdapter {

    private Context mContext;
    private ArrayList<HelpNoticeEntry> mEntry;
    private GroupHolder groupHolder = null;
    private ChildHolder childHolder = null;

    public HelpExpandableAdapter(Context mContext, ArrayList<HelpNoticeEntry> mEntry) {
        this.mContext = mContext;
        this.mEntry = mEntry;
    }

    @Override
    public int getGroupCount() {
        return this.mEntry.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return 1;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this.mEntry.get(groupPosition).getTitle();
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return this.mEntry.get(groupPosition).getContents();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return 0;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        View v = convertView;

        if (v == null) {
            groupHolder = new GroupHolder();
            LayoutInflater inflater = LayoutInflater.from(mContext);
            v = inflater.inflate(R.layout.holder_expandable_group, null);
            groupHolder.group_title = (TextView) v.findViewById(R.id.group_title);
            groupHolder.group_image = (ImageView) v.findViewById(R.id.group_image);
            v.setTag(groupHolder);
        } else {
            groupHolder = (GroupHolder) v.getTag();
        }

        if (getGroup(groupPosition) != null) {
            String fullText = getGroup(groupPosition) + " " + this.mEntry.get(groupPosition).getInsertDate();
            //글자색 변경
            final SpannableStringBuilder sps = new SpannableStringBuilder();

            SpannableString ss = new SpannableString(fullText);
            ss.setSpan(new ForegroundColorSpan(mContext.getResources().getColor(R.color.colorPrimary)), ((String)getGroup(groupPosition)).length() + 1, fullText.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            ss.setSpan(new RelativeSizeSpan(0.7f), ((String)getGroup(groupPosition)).length() + 1, fullText.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            sps.append(ss);
            groupHolder.group_title.setText(sps);
        }

        if (isExpanded)
            groupHolder.group_image.setImageResource(R.drawable.ic_keyboard_arrow_up_black_24dp);
        else
            groupHolder.group_image.setImageResource(R.drawable.ic_keyboard_arrow_down_black_24dp);

        return v;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        View v = convertView;

        if (v == null) {
            childHolder = new ChildHolder();
            LayoutInflater inflater = LayoutInflater.from(mContext);
            v = inflater.inflate(R.layout.holder_expandable_child, null);
            childHolder.child_content = (TextView) v.findViewById(R.id.child_content);
            v.setTag(childHolder);
        } else {
            childHolder = (ChildHolder) v.getTag();
        }

        if (getChild(groupPosition, 0) != null) {
            childHolder.child_content.setText(Html.fromHtml((String)getChild(groupPosition, 0)));
        }

        return v;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

    private class GroupHolder {
        TextView group_title;
        ImageView group_image;
    }

    private class ChildHolder {
        TextView child_content;
    }
}
