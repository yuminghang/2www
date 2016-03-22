package com.delin.dgclient.view;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.TextView;

import com.delin.dgclient.R;


public class CustomDialog extends Dialog {

    public CustomDialog(Context context) {
        super(context);
    }

    public CustomDialog(Context context, int theme) {
        super(context, theme);
    }

    public static class Builder {
        private Context context;
        private String title;
        private String message;
        private String positiveButtonText;
        private String negativeButtonText;

        private OnClickListener positiveButtonClickListener;
        private OnClickListener negativeButtonClickListener;
        private CustomDialog dialog;
        public Boolean isCreated = false;
        private String message2;
        private String num;

        public Builder(Context context) {
            this.context = context;
        }

        public Builder setMessage2(String message2){
            this.message2 = message2;
            return this;
        }

        public Builder setNum(String num){
            this.num =num;
            return this;
        }

        public Builder setMessage(String message) {
            this.message = message;
            return this;
        }

        /**
         * Set the Dialog title from resource
         *
         * @param title
         * @return
         */
        public Builder setTitle(int title) {
            this.title = (String) context.getText(title);
            return this;
        }

        /**
         * Set the Dialog title from String
         *
         * @param title
         * @return
         */

        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        /**
         * Set the positive button resource and it's listener
         *
         * @param positiveButtonText
         * @param listener
         * @return
         */
        public Builder setPositiveButton(int positiveButtonText,
                                         OnClickListener listener) {
            this.positiveButtonText = (String) context
                    .getText(positiveButtonText);
            this.positiveButtonClickListener = listener;
            return this;
        }

        public Builder setPositiveButton(String positiveButtonText,
                                         OnClickListener listener) {
            this.positiveButtonText = positiveButtonText;
            this.positiveButtonClickListener = listener;
            return this;
        }

        public Builder setNegativeButton(int negativeButtonText,
                                         OnClickListener listener) {
            this.negativeButtonText = (String) context
                    .getText(negativeButtonText);
            this.negativeButtonClickListener = listener;
            return this;
        }

        public Builder setNegativeButton(String negativeButtonText,
                                         OnClickListener listener) {
            this.negativeButtonText = negativeButtonText;
            this.negativeButtonClickListener = listener;
            return this;
        }

        public CustomDialog create() {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            dialog= new CustomDialog(context,R.style.Dialog);
            View layout = inflater.inflate(R.layout.kongfu_pande_dialogr, null);
            dialog.addContentView(layout, new LayoutParams(
                    LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
            // set the dialog title
            ((TextView) layout.findViewById(R.id.msgTextView)).setText(title);
            layout.findViewById(R.id.dismissButton).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dismiss();
                }
            });

            // set the confirm button
            if (positiveButtonText != null) {
                ((Button) layout.findViewById(R.id.positiveButton))
                        .setText(positiveButtonText);
                if (positiveButtonClickListener != null) {
                    (layout.findViewById(R.id.positiveButton))
                            .setOnClickListener(new View.OnClickListener() {
                                public void onClick(View v) {
                                    positiveButtonClickListener.onClick(dialog,
                                            DialogInterface.BUTTON_POSITIVE);
                                }
                            });
                }
            } else {
                // if no confirm button just set the visibility to GONE
                layout.findViewById(R.id.positiveButton).setVisibility(
                        View.GONE);
            }
            // set the cancel button
            if (negativeButtonText != null) {
                ((Button) layout.findViewById(R.id.negativeButton))
                        .setText(negativeButtonText);
                if (negativeButtonClickListener != null) {
                    (layout.findViewById(R.id.negativeButton))
                            .setOnClickListener(new View.OnClickListener() {
                                public void onClick(View v) {
                                    negativeButtonClickListener.onClick(dialog,
                                            DialogInterface.BUTTON_NEGATIVE);
                                }
                            });
                }
            } else {
                // if no confirm button just set the visibility to GONE
                layout.findViewById(R.id.negativeButton).setVisibility(
                        View.GONE);
            }
            Button continueGameButton = (Button) layout.findViewById(R.id.continueGameButton);
            continueGameButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dismiss();
                }
            });
            // set the content message
            if (message != null) {
                layout.findViewById(R.id.kongfuPanderImageView).setBackgroundResource(R.mipmap.xiongmao_04);
                ((TextView)layout.findViewById(R.id.msgTextView)).setText(message);
                continueGameButton.setVisibility(View.GONE);
                layout.findViewById(R.id.message2TextView).setVisibility(View.GONE);
            }else {
                layout.findViewById(R.id.kongfuPanderImageView).setBackgroundResource(R.mipmap.xiongmao_03);
                (layout.findViewById(R.id.msgT1extView)).setVisibility(View.GONE);
                continueGameButton.setVisibility(View.GONE);
                layout.findViewById(R.id.message2TextView).setVisibility(View.GONE);
            }
            if (message2!=null){
                (layout.findViewById(R.id.negativeButton)).setVisibility(View.GONE);
                (layout.findViewById(R.id.positiveButton)).setVisibility(View.GONE);
                continueGameButton.setVisibility(View.VISIBLE);
                layout.findViewById(R.id.dismissButton).setBackgroundResource(R.mipmap.cancel_red);
                continueGameButton.setBackgroundResource(R.mipmap.icon_red);
                layout.findViewById(R.id.message2TextView).setVisibility(View.VISIBLE);
                ((TextView)layout.findViewById(R.id.message2TextView)).setText(message2);
                ((TextView)layout.findViewById(R.id.message2TextView)).setTextColor(layout.getResources().getColor(R.color.red));
                layout.findViewById(R.id.kongfuPanderImageView).setBackgroundResource(R.mipmap.panda_red);

            }

            if (num!=null){
                layout.findViewById(R.id.kongfuPanderImageView).setBackgroundResource(R.mipmap.gift_07);
                layout.findViewById(R.id.message2TextView).setVisibility(View.VISIBLE);
                ((TextView)layout.findViewById(R.id.message2TextView)).setText(message2);
                continueGameButton.setVisibility(View.VISIBLE);
                layout.findViewById(R.id.msg0TextView).setVisibility(View.GONE);
                layout.findViewById(R.id.dismissButton).setBackgroundResource(R.mipmap.cancel_red);
                continueGameButton.setBackgroundResource(R.mipmap.icon_red);
                continueGameButton.setText("知道了");
            }

            dialog.setContentView(layout);
            dialog.setCancelable(false);
            isCreated = true;
            return dialog;
        }

        public void dismiss(){
            isCreated = false;
            dialog.dismiss();
        }
    }
}
