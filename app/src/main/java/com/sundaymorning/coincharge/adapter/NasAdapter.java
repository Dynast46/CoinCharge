package com.sundaymorning.coincharge.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nextapps.naswall.NASWall;
import com.nextapps.naswall.NASWallAdInfo;
import com.squareup.picasso.Picasso;
import com.sundaymorning.coincharge.R;
import com.sundaymorning.coincharge.holder.NasViewHolder;
import com.sundaymorning.coincharge.object.NasEntry;

import java.util.ArrayList;

/**
 * Created by Dynast on 2017. 5. 7..
 */

public class NasAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<NasEntry> mEntries;
    private ArrayList<NASWallAdInfo> mAdInfos;
    private NASWallAdInfo mInfo;
    private NASWall.OnJoinAdListener mOnJoinAdListener = new NASWall.OnJoinAdListener() {
        @Override
        public void OnSuccess(NASWallAdInfo nasWallAdInfo, String s) {
            boolean isSuccess = false;
            try {
                Intent intent = Intent.parseUri(s, 0);
                if (intent != null) {
                    mContext.startActivity(intent);
                    isSuccess = true;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (!isSuccess) {
                Toast.makeText(mContext, "캠페인에 참여할 수 없습니다.", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void OnError(NASWallAdInfo nasWallAdInfo, int i) {
            // 참여 실패
            String message = "[" + i + "] ";

            switch (i) {
                case -10001:
                    message += "종료된 캠페인입니다.";
                    break;
                case -20001:
                    message += "이미 참여한 캠페인입니다.";
                    break;
                default:
                    message += "캠페인에 참여할 수 없습니다.";
                    break;
            }

            Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void OnComplete(NASWallAdInfo nasWallAdInfo) {

        }
    };
    private DialogInterface.OnClickListener mOKClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            NASWall.joinAd(mContext, mInfo, mOnJoinAdListener);
        }
    };
    private NASWall.OnAdDescriptionListener mOnDescriptionListener = new NASWall.OnAdDescriptionListener() {
        @Override
        public void OnSuccess(NASWallAdInfo nasWallAdInfo, String s) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View layout = inflater.inflate(R.layout.activity_detail, null);
            TextView title = (TextView) layout.findViewById(R.id.text_detail_title);
            title.setText(nasWallAdInfo.getTitle());

            TextView description = (TextView) layout.findViewById(R.id.text_detail_description);
            description.setText(nasWallAdInfo.getMissionText());
            TextView reward = (TextView) layout.findViewById(R.id.text_detail_reward);
            reward.setText(String.format("%s 코인", String.valueOf(nasWallAdInfo.getRewardPrice())));

            ImageView image = (ImageView) layout.findViewById(R.id.detail_image);
            Picasso.with(mContext).load(nasWallAdInfo.getIconUrl()).into(image);

            TextView contents = (TextView) layout.findViewById(R.id.text_detail_content);
            contents.setText(s);

            AlertDialog.Builder alt_bld = new AlertDialog.Builder(mContext);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                alt_bld.setView(layout);
            }

            alt_bld.setCancelable(false);
            alt_bld.setPositiveButton(mContext.getString(R.string.ok), mOKClickListener);
            alt_bld.setNegativeButton(mContext.getString(R.string.cancel), null);

            AlertDialog alert = alt_bld.create();
            alert.setTitle(mContext.getString(R.string.ads_detail));
            alert.show();

        }

        @Override
        public void OnError(NASWallAdInfo nasWallAdInfo, int i) {

        }
    };
    private View.OnClickListener JoinOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            NASWallAdInfo info = (NASWallAdInfo) v.getTag();
            mInfo = info;
            NASWall.getAdDescription(mContext, info, mOnDescriptionListener);
        }
    };

    public NasAdapter(Context context, ArrayList<NasEntry> entries, ArrayList<NASWallAdInfo> adInfos) {
        this.mContext = context;
        this.mEntries = entries;
        this.mAdInfos = adInfos;
    }

    @Override
    public int getCount() {
        return mEntries.size();
    }

    @Override
    public Object getItem(int position) {
        return mEntries.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        NasViewHolder nasViewHolder;

        if (v == null) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            v = inflater.inflate(R.layout.holder_nas, parent, false);
            nasViewHolder = new NasViewHolder(v);
            v.setTag(nasViewHolder);
        } else {
            nasViewHolder = (NasViewHolder) v.getTag();
        }

        if (getItem(position) != null) {
            NasEntry entry = (NasEntry) getItem(position);

            nasViewHolder.setIconImage(mContext, entry.getImageURL());
            nasViewHolder.setTitle(entry.getTitle());
            if (entry.getSubtitle().equals(""))
                nasViewHolder.getSubTitle().setVisibility(View.GONE);
            else {
                nasViewHolder.getSubTitle().setVisibility(View.VISIBLE);
                nasViewHolder.setSubTitle(entry.getSubtitle());
            }

            if (mAdInfos.get(position).getJoinStatus() == NASWallAdInfo.JoinStatus.JOIN)
                nasViewHolder.setCashBtn(mContext.getString(R.string.current_join));
            else
                nasViewHolder.setCashBtn(entry.getBtnText() + " 코인");

            nasViewHolder.getCashBtn().setTag(mAdInfos.get(position));
            nasViewHolder.getCashBtn().setOnClickListener(JoinOnClickListener);
        }

        return v;
    }
}