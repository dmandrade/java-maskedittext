package com.dmandrade.maskedittext;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatEditText;
import android.util.AttributeSet;
import com.dmandrade.maskedittext.R.styleable;


public final class MaskEditText extends AppCompatEditText {
    private boolean selfChange;
    private String mask;

    @Nullable
    public final String getRawText() {
        return this.unformat((CharSequence)this.getText());
    }

    private final void init(Context context, AttributeSet attrs) {
        if (attrs != null) {
            TypedArray typedArray = context.obtainStyledAttributes(attrs, styleable.MaskEditText);
            if (typedArray.hasValue(styleable.MaskEditText_mask)) {
                this.mask = typedArray.getString(styleable.MaskEditText_mask);
            }

            if (typedArray != null) {
                typedArray.recycle();
            }
        }

    }

    protected void onTextChanged(@Nullable CharSequence text, int start, int lengthBefore, int lengthAfter) {
        if (text != null && text.length() != 0 && !this.selfChange) {
            this.selfChange = true;
            this.format(text);
            this.setCursorPosition(start, lengthBefore, lengthAfter);
            this.selfChange = false;
        }
    }

    private final void format(CharSequence source) {
        if (source != null && source.length() != 0) {
            CharSequence maskCharSequence = (CharSequence)this.mask;
            if (maskCharSequence != null && maskCharSequence.length() != 0) {
                StringBuilder builder = new StringBuilder();

                int textLength = source.length();
                int textIndex = 0;
                if (this.mask != null) {
                    for(int i = 0; i < maskCharSequence.length(); ++i) {
                        char element$iv = maskCharSequence.charAt(i);
                        if (textIndex < textLength) {
                            char c = source.charAt(textIndex);
                            if (Extensions.isPlaceHolder(element$iv)) {
                                if (Character.isLetterOrDigit(c)) {
                                    builder.append(c);
                                    ++textIndex;
                                } else {
                                    int u = textIndex;

                                    for(; u < textLength; ++u) {
                                        c = source.charAt(u);
                                        if (Character.isLetterOrDigit(c)) {
                                            builder.append(c);
                                            textIndex = u + 1;
                                            break;
                                        }
                                    }
                                }
                            } else {
                                builder.append(element$iv);
                                if (c == element$iv) {
                                    ++textIndex;
                                }
                            }
                        }
                    }
                }

                this.setText((CharSequence)builder);
                return;
            }
        }

    }

    private final String unformat(CharSequence source) {
        if (source != null && source.length() != 0) {
            CharSequence maskCharSequence = (CharSequence)this.mask;
            if (maskCharSequence != null && maskCharSequence.length() != 0) {
                StringBuilder builder = new StringBuilder();

                int textLength = source.length();
                if (this.mask != null) {
                    int textIndex = 0;

                    for(int i = 0; i < maskCharSequence.length(); ++i) {
                        char item$iv = maskCharSequence.charAt(i);
                        int index = textIndex++;
                        if (index < textLength) {
                            char c = source.charAt(index);
                            if (Extensions.isPlaceHolder(item$iv)) {
                                builder.append(c);
                            }
                        }
                    }
                }

                return builder.toString();
            }
        }

        return null;
    }

    private final void setCursorPosition(int start, int lengthBefore, int lengthAfter) {
        CharSequence textCharSequence = (CharSequence)this.getText();
        if (textCharSequence != null && textCharSequence.length() != 0) {
            int end = this.getText().length();
            int cursor = lengthBefore > lengthAfter ? start : (lengthAfter > 1 ? end : (start < end ? this.findNextPlaceHolderPosition(start, end) : end));
            this.setSelection(cursor);
        }
    }

    private final int findNextPlaceHolderPosition(int start, int end) {
        if (this.mask != null) {
            int i = start;

            for(; i < end; ++i) {
                char m = this.mask.charAt(i);
                char c = this.getText().charAt(i);
                if (Extensions.isPlaceHolder(m) && Character.isLetterOrDigit(c)) {
                    return i + 1;
                }
            }
        }

        return start + 1;
    }

    public MaskEditText(Context context) {
        super(context);
    }

    public MaskEditText(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.init(context, attrs);
    }

    public MaskEditText(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.init(context, attrs);
    }
}
