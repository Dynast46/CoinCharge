package com.sundaymorning.coincharge.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.namember.utils.JsonUtil;
import com.namember.utils.NAMemberUtils;
import com.namember.utils.NetManager;
import com.sundaymorning.coincharge.R;
import com.sundaymorning.coincharge.adapter.StoreAdapter;
import com.sundaymorning.coincharge.data.MemberInfoData;
import com.sundaymorning.coincharge.data.SharedPreferenceUtils;
import com.sundaymorning.coincharge.object.StoreCardEntry;
import com.sundaymorning.coincharge.utils.Utils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by sweet on 2017-08-05.
 */

@SuppressLint("ValidFragment")
public class StoreFragment extends Fragment {

    private Context mContext;
    private View v;
    private MemberInfoData memberInfoData;

    public StoreFragment(Context mContext) {
        this.mContext = mContext;
    }

    public interface onStoreCardListener {
        void OnSuccess(ArrayList<StoreCardEntry> var4);

        void OnError(int var1);

        void OnJson(String var1);
    }

    public interface onCardPurchaseListener {
        void OnSuccess(int MEMBER_MONEY);

        void OnError(int var1);

        void OnJson(String var1);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_store, container, false);

        getActivity().setTitle(R.string.store);

        StoreCardList(mContext, 2, new onStoreCardListener() {
            @Override
            public void OnSuccess(ArrayList<StoreCardEntry> var4) {
                StoreAdapter adapter = new StoreAdapter(mContext, var4);
                ListView listView = (ListView) v.findViewById(R.id.adlistView);
                listView.setAdapter(adapter);
                listView.setOnItemClickListener(mOnItemClickListener);
            }

            @Override
            public void OnError(int var1) {

            }

            @Override
            public void OnJson(String var1) {

            }
        });

        return v;
    }

    private AdapterView.OnItemClickListener mOnItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            CardPurchaseInsert(mContext, (int)id, new onCardPurchaseListener() {
                @Override
                public void OnSuccess(int MEMBER_MONEY) {
                    memberInfoData = SharedPreferenceUtils.loadMemberInfoData(mContext);
                    memberInfoData.setMoney(MEMBER_MONEY);

                    Utils.showDialog(mContext,
                            null,
                            "구매가 정상적으로 이루어졌습니다.",
                            getResources().getString(R.string.ok),
                            null,
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            }, null);
                }

                @Override
                public void OnError(int var1) {
                    Utils.showDialog(mContext,
                            null,
                            "[" + var1 + "] " + getResources().getString(R.string.re_try_message),
                            getResources().getString(R.string.ok),
                            null,
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            }, null);
                }

                @Override
                public void OnJson(String var1) {

                }
            });
        }
    };

    public void StoreCardList(final Context context, final int CARD_CATEGORY_ID, final onStoreCardListener onstoreCardListener) {
        if (NAMemberUtils.checkNetworkAvailable(context)) {
            (new Thread(new Runnable() {
                public void run() {
                    final String json = NetManager.execute(mContext, "json/CardInfoList.json.asp?", "CARD_CATEGORY_ID", String.valueOf(CARD_CATEGORY_ID));
                    ((Activity)mContext).runOnUiThread(new Runnable() {
                        public void run() {
                            onstoreCardListener.OnJson(json);
                        }
                    });

                    try {
                        JSONObject object = new JSONObject(json);
                        StoreCardparse(mContext, object, onstoreCardListener);
                    } catch (Exception var7) {
                        ((Activity)context).runOnUiThread(new Runnable() {
                            public void run() {
                                onstoreCardListener.OnError(-999);
                            }
                        });
                    }

                }
            })).start();
        }
    }

    private void StoreCardparse(Context context, final JSONObject object, final onStoreCardListener onstoreCardListener) throws Exception {
        if(object != null) {
            if(JsonUtil.getJsonInt(object, "result", -999) == 0) {
                final ArrayList<StoreCardEntry> StoreCardDataList = new ArrayList<>();
                JSONArray array = object.getJSONArray("items");

                for(int i = 0; i < array.length(); ++i) {
                    int card_type_id = JsonUtil.getJsonInt(array.getJSONObject(i), "CARD_TYPE_ID", 0);
                    int market_money = JsonUtil.getJsonInt(array.getJSONObject(i), "MARKET_MONEY", 0);
                    String name = JsonUtil.getJsonString(array.getJSONObject(i), "NAME", "");
                    int use_possible_card_count = JsonUtil.getJsonInt(array.getJSONObject(i), "USE_POSSIBLE_CARD_COUNT", 0);

                    StoreCardDataList.add(new StoreCardEntry(card_type_id, market_money, name, use_possible_card_count));
                }

                ((Activity)context).runOnUiThread(new Runnable() {
                    public void run() {
                        onstoreCardListener.OnSuccess(StoreCardDataList);
                    }
                });
            } else {
                ((Activity)context).runOnUiThread(new Runnable() {
                    public void run() {
                        onstoreCardListener.OnError(JsonUtil.getJsonInt(object, "result", -999));
                    }
                });
            }
        } else {
            ((Activity)context).runOnUiThread(new Runnable() {
                public void run() {
                    onstoreCardListener.OnError(-999);
                }
            });
        }
    }

    public void CardPurchaseInsert(final Context context, final int CARD_TYPE_ID, final onCardPurchaseListener oncardPurchaseListener) {
        if (NAMemberUtils.checkNetworkAvailable(context)) {
            (new Thread(new Runnable() {
                public void run() {
                    final String json = NetManager.execute(mContext, "json/CardPurchaseInsert.json.asp?", "CARD_TYPE_ID", String.valueOf(CARD_TYPE_ID));
                    ((Activity)mContext).runOnUiThread(new Runnable() {
                        public void run() {
                            oncardPurchaseListener.OnJson(json);
                        }
                    });

                    try {
                        JSONObject object = new JSONObject(json);
                        CardPurchaseparse(mContext, object, oncardPurchaseListener);
                    } catch (Exception var7) {
                        ((Activity)context).runOnUiThread(new Runnable() {
                            public void run() {
                                oncardPurchaseListener.OnError(-999);
                            }
                        });
                    }

                }
            })).start();
        }
    }

    private void CardPurchaseparse(Context context, final JSONObject object, final onCardPurchaseListener onstoreCardListener) throws Exception {
        if(object != null) {
            if(JsonUtil.getJsonInt(object, "result", -999) == 0) {
                final int MEMBER_MONEY = JsonUtil.getJsonInt(object, "MEMBER_MONEY", 0);
                ((Activity)context).runOnUiThread(new Runnable() {
                    public void run() {
                        onstoreCardListener.OnSuccess(MEMBER_MONEY);
                    }
                });
            } else {
                ((Activity)context).runOnUiThread(new Runnable() {
                    public void run() {
                        onstoreCardListener.OnError(JsonUtil.getJsonInt(object, "result", -999));
                    }
                });
            }
        } else {
            ((Activity)context).runOnUiThread(new Runnable() {
                public void run() {
                    onstoreCardListener.OnError(-999);
                }
            });
        }
    }
}
