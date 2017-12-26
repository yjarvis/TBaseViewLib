package com.jarvis.tbaseviewlib.widget;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.jarvis.tbaseviewlib.R;


/**
 * 功能说明 重新封装带按钮的Dialog类
 * <p/>
 * 作者: jarvisT
 */
public class TDialog {

    private Builder builder;
    private Dialog dialog;
    private DialogListener listener = null;
    private TextView messageText;
    private Button okBtn;
    private View lineview;
    private Button cancelBtn;
    private TextView titleText;
    private Context context;
    private String title = "";
    private String message = "";
    /**
     * 是否显示取消按钮，true显示，false不显示
     */
    private boolean isShowCancel = true;

    /**
     * @param context
     */
    public TDialog(Context context) {
        this.context = context;
    }

    /**
     * @param context
     * @param title   提示的文本 （为null或者""则默认为""）
     * @param message 提示的文本 （为null或者""则默认为""）
     * @param cancel  取消键的文本（为null或者""则默认为取消）
     * @param ok      确定键的文本（为null或者""则默认为确认）
     * @return
     */
    public TDialog(Context context, String title, String message, String cancel, String ok) {
        this.title = title;
        this.message = message;
        initDialogContent(context, cancel, ok);
    }


    /**
     * @param context
     * @param title    提示的文本 （为null或者""则默认为""）
     * @param message  提示的文本 （为null或者""则默认为""）
     * @param cancel   取消键的文本（为null或者""则默认为取消）
     * @param ok       确定键的文本（为null或者""则默认为确认）
     * @param listener 确定/取消键的监听（为null或者则默认不处理）
     * @return
     */
    public TDialog(Context context, String title, String message, String cancel, String ok, DialogListener listener) {
        this.listener = listener;
        this.title = title;
        this.message = message;
        initDialogContent(context, cancel, ok);
    }

    /**
     * @param context
     * @param title        提示的文本 （为null或者""则默认为""）
     * @param message      提示的文本 （为null或者""则默认为""）
     * @param cancel       取消键的文本（为null或者""则默认为取消）
     * @param ok           确定键的文本（为null或者""则默认为确认）
     * @param isShowCancel 是否显示取消按钮，true显示，false不显示
     * @return
     */
    public TDialog(Context context, String title, String message, String cancel, String ok, boolean isShowCancel) {
        this.isShowCancel = isShowCancel;
        this.title = title;
        this.message = message;
        initDialogContent(context, cancel, ok);
    }

    /**
     * @param context
     * @param title        提示的文本 （为null或者""则默认为""）
     * @param message      提示的文本 （为null或者""则默认为""）
     * @param cancel       取消键的文本（为null或者""则默认为取消）
     * @param ok           确定键的文本（为null或者""则默认为确认）
     * @param isShowCancel 是否显示取消按钮，true显示，false不显示
     * @param noShow       不立刻弹出，需要调用show方法才会弹出
     * @return
     */
    public TDialog(Context context, String title, String message, String cancel, String ok, boolean isShowCancel, boolean noShow) {
        this.title = title;
        this.isShowCancel = isShowCancel;
        this.message = message;
        initDialogContent2(context, cancel, ok);
    }

    /**
     * @param context
     * @param title        提示的文本 （为null或者""则默认为""）
     * @param message      提示的文本 （为null或者""则默认为""）
     * @param cancel       取消键的文本（为null或者""则默认为取消）
     * @param ok           确定键的文本（为null或者""则默认为确认）
     * @param isShowCancel 是否显示取消按钮，true显示，false不显示
     * @param listener     确定/取消键的监听（为null或者则默认不处理）
     * @return
     */
    public TDialog(Context context, String title, String message, String cancel, String ok, boolean isShowCancel, DialogListener listener) {
        this.listener = listener;
        this.isShowCancel = isShowCancel;
        this.title = title;
        this.message = message;
        initDialogContent(context, cancel, ok);
    }


    private void initDialogContent(Context context, String negative, String positive) {
        dialog = new Dialog(context, R.style.TCustomDialog);
        View view = LayoutInflater.from(context).inflate(R.layout.tdialog_layout, null);
        okBtn = (Button) view.findViewById(R.id.dialog_ok);
        lineview = view.findViewById(R.id.dialog_lineview);
        cancelBtn = (Button) view.findViewById(R.id.dialog_cancel);
        messageText = (TextView) view.findViewById(R.id.dialog_message);
        titleText = (TextView) view.findViewById(R.id.dialog_title);
        dialog.setContentView(view);
        //禁止点击边缘消失
        dialog.setCanceledOnTouchOutside(false);
        //禁止点击返回按钮消失
        dialog.setCancelable(false);
        if (!TextUtils.isEmpty(message)) {
            messageText.setText(message);
        } else {
            messageText.setText("");
        }

        if (!TextUtils.isEmpty(title)) {
            titleText.setText(title);
        } else {
            titleText.setText("");
            titleText.setVisibility(View.GONE);
        }
        // 取消键的文本（为null或者""则显示"取消"文本）
        if (!isShowCancel) {
            cancelBtn.setVisibility(View.GONE);
            lineview.setVisibility(View.GONE);
            dialog.setCanceledOnTouchOutside(false);
        }
        if (!TextUtils.isEmpty(negative)) {
            cancelBtn.setText(negative);
        } else {
            cancelBtn.setText("取消");
        }
        // 取消键的文本（为null或者""则“确定”文本）
        if (!TextUtils.isEmpty(positive)) {
            okBtn.setText(positive);
        } else {
            okBtn.setText("确定");
        }

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (listener != null) {
                    listener.cancelClick();
                }
            }
        });

        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (listener != null) {
                    listener.okClick(-1);
                }
            }
        });
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                if (listener != null) {
                    listener.dissmissClick();
                }
            }
        });

        dialog.show();
    }

    private void initDialogContent2(Context context, String negative, String positive) {
        dialog = new Dialog(context, R.style.TCustomDialog);
        View view = LayoutInflater.from(context).inflate(R.layout.tdialog_layout, null);
        okBtn = (Button) view.findViewById(R.id.dialog_ok);
        lineview = view.findViewById(R.id.dialog_lineview);
        cancelBtn = (Button) view.findViewById(R.id.dialog_cancel);
        messageText = (TextView) view.findViewById(R.id.dialog_message);
        titleText = (TextView) view.findViewById(R.id.dialog_title);
        dialog.setContentView(view);
        //禁止点击边缘消失
        dialog.setCanceledOnTouchOutside(false);
        //禁止点击返回按钮消失
        dialog.setCancelable(false);
        if (!TextUtils.isEmpty(message)) {
            messageText.setText(message);
        } else {
            messageText.setText("");
        }

        if (!TextUtils.isEmpty(title)) {
            titleText.setText(title);
        } else {
            titleText.setText("");
            titleText.setVisibility(View.GONE);
        }
        // 取消键的文本（为null或者""则显示"取消"文本）
        if (!isShowCancel) {
            cancelBtn.setVisibility(View.GONE);
            lineview.setVisibility(View.GONE);
            dialog.setCanceledOnTouchOutside(false);
        }
        if (!TextUtils.isEmpty(negative)) {
            cancelBtn.setText(negative);
        } else {
            cancelBtn.setText("取消");
        }
        // 取消键的文本（为null或者""则“确定”文本）
        if (!TextUtils.isEmpty(positive)) {
            okBtn.setText(positive);
        } else {
            okBtn.setText("确定");
        }

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (listener != null) {
                    listener.cancelClick();
                }
            }
        });

        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (listener != null) {
                    listener.okClick(-1);
                }
            }
        });
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                if (listener != null) {
                    listener.dissmissClick();
                }
            }
        });
    }

    public interface DialogListener {
        /**
         * 重写实现Dialog的确定键点击监听
         */
        public void okClick(int position);

        /**
         * 重写实现Dialog的取消键点击监听
         */
        public void cancelClick();

        /**
         * 重写实现Dialog消失的时候监听
         */
        public void dissmissClick();

    }

    /**
     * 显示多个内容单选的Dialog
     *
     * @param items
     * @param listener
     * @author tansheng
     */
    public void showItemsDialog(String[] items, final DialogListener listener) {
        builder = new AlertDialog.Builder(context);
        builder.setItems(items, new OnClickListener() {

            @Override
            public void onClick(DialogInterface arg0, int position) {
                if (listener != null) {
                    listener.okClick(position);
                }
            }
        });
        builder.create();
        builder.show();
    }

    /**
     * 是否弹出
     *
     * @return
     * @author tansheng QQ:717549357
     * @date 2015-12-16 下午4:29:58
     */
    public boolean isShown() {
        return dialog.isShowing();
    }

    /**
     * 设置自定义风格的自定义界面
     *
     * @param layoutId 自定义界面
     * @param styleId  自定义风格，使用默认风格传0
     * @return 返回自定义的界面
     */
    public View setView(int layoutId, int styleId) {
        if (styleId <= 0) {
            dialog = new Dialog(context, R.style.TCustomDialog);
        } else {
            dialog = new Dialog(context, styleId);
        }
        View view = LayoutInflater.from(context).inflate(layoutId, null);
        dialog.setContentView(view);
        //禁止点击边缘消失
        dialog.setCanceledOnTouchOutside(false);
        //禁止点击返回按钮消失
        dialog.setCancelable(false);
        return view;
    }

    /**
     * 设置默认风格的自定义界面
     *
     * @param layoutId 自定义界面
     * @return 返回自定义的界面
     */
    public View setView(int layoutId) {
        dialog = new Dialog(context, R.style.TCustomDialog);
        View view = LayoutInflater.from(context).inflate(layoutId, null);
        dialog.setContentView(view);
        //禁止点击边缘消失
        dialog.setCanceledOnTouchOutside(false);
        //禁止点击返回按钮消失
        dialog.setCancelable(false);
        return view;
    }

    public void setTitle(String title) {
        if (titleText == null) {
            return;
        }
        this.title = title;
        if (!TextUtils.isEmpty(title)) {
            titleText.setText(title);
            titleText.setVisibility(View.VISIBLE);
        } else {
            titleText.setText("");
            titleText.setVisibility(View.GONE);
        }
    }

    public void setMessage(String message) {
        if (messageText == null) {
            return;
        }
        this.message = message;
        if (!TextUtils.isEmpty(message)) {
            messageText.setText(message);
        } else {
            messageText.setText("");
        }
    }

    /**
     * 点击边缘是否消失
     *
     * @param isshow
     */
    public void setCanceledOnTouchOutside(boolean isshow) {
        if (dialog == null) {
            return;
        }
        dialog.setCanceledOnTouchOutside(isshow);
    }

    /**
     * 点击返回按钮是否消失
     *
     * @param isshow
     */
    public void setCancelable(boolean isshow) {
        if (dialog == null) {
            return;
        }
        dialog.setCancelable(isshow);
    }

    public void setListener(DialogListener listener) {
        this.listener = listener;
    }

    public void show() {
        if (dialog != null && !dialog.isShowing()) {
            dialog.show();
        }
    }

    public void dismiss() {
        if (dialog != null) {
            dialog.dismiss();
        }
    }

}
