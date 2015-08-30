package com.nethergrim.vk.fragment;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import com.nethergrim.vk.Constants;
import com.nethergrim.vk.MyApplication;
import com.nethergrim.vk.R;
import com.nethergrim.vk.activity.AbstractActivity;
import com.nethergrim.vk.adapter.ChatAdapter;
import com.nethergrim.vk.caching.Prefs;
import com.nethergrim.vk.models.Conversation;
import com.nethergrim.vk.models.User;
import com.nethergrim.vk.utils.ConversationUtils;
import com.nethergrim.vk.utils.UserProvider;
import com.nethergrim.vk.web.WebIntentHandler;
import com.nethergrim.vk.web.WebRequestManager;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import github.ankushsachdeva.emojicon.EmojiconEditText;
import github.ankushsachdeva.emojicon.EmojiconGridView;
import github.ankushsachdeva.emojicon.EmojiconsPopup;
import github.ankushsachdeva.emojicon.emoji.Emojicon;
import io.realm.Realm;

/**
 * @author andrej on 07.08.15.
 */
public class ChatFragment extends AbstractFragment
        implements Toolbar.OnMenuItemClickListener {

    public static final String EXTRA_CONVERSATION_ID = Constants.PACKAGE_NAME + ".CONV_ID";
    @Inject
    Realm mRealm;
    @Inject
    WebIntentHandler mWebIntentHandler;
    @Inject
    UserProvider mUserProvider;
    @Inject
    Prefs mPrefs;
    @InjectView(R.id.toolbar)
    Toolbar mToolbar;
    @Inject
    WebRequestManager mWebRequestManager;
    @InjectView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @InjectView(R.id.et_message)
    EmojiconEditText mEtMessage;
    @InjectView(R.id.btn_emoji)
    ImageButton mBtnEmoji;
    @InjectView(R.id.btn_send)
    ImageButton mBtnSend;
    @InjectView(R.id.inputMessagesController)
    RelativeLayout mInputMessagesController;
    private long mConversationId;
    private Conversation mConversation;
    private boolean mIsGroupChat;
    private User mAnotherUser;

    public static ChatFragment getInstance(long conversationId, boolean isAGroupChat) {
        ChatFragment chatFragment = new ChatFragment();
        Bundle args = new Bundle();
        args.putLong(EXTRA_CONVERSATION_ID, conversationId);
        args.putBoolean(Constants.EXTRA_GROUP_CONVERSATION, isAGroupChat);
        chatFragment.setArguments(args);
        return chatFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mConversationId = getConversationIdFromExtras(savedInstanceState);
        MyApplication.getInstance().getMainComponent().inject(this);
        setHasOptionsMenu(true);
        setRetainInstance(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_chat, container, false);
        ButterKnife.inject(this, v);
        initViews();
        return v;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        loadConversation();
        initToolbar();
        loadLastMessages();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_chat, menu);
        if (mIsGroupChat) {
            menu.removeItem(R.id.action_user_profile);
        } else {
            menu.removeItem(R.id.action_group_conversation_details);
        }
    }


    @Override
    public boolean onMenuItemClick(MenuItem item) {
        Log.e("TAG", "menu item click " + item.getTitle());
        // TODO handle
        return false;
    }

    private long getConversationIdFromExtras(Bundle extras) {
        Bundle args = getArguments();
        if (args != null && args.containsKey(EXTRA_CONVERSATION_ID)) {
            mIsGroupChat = args.getBoolean(Constants.EXTRA_GROUP_CONVERSATION, false);
            return args.getLong(EXTRA_CONVERSATION_ID);
        } else if (extras != null && extras.containsKey(EXTRA_CONVERSATION_ID)) {
            return extras.getLong(EXTRA_CONVERSATION_ID);
        } else
            return 0;
    }

    private void initList(Context context) {
        mRecyclerView.setAdapter(new ChatAdapter(mConversationId, mIsGroupChat));
        mRecyclerView.setLayoutManager(
                new LinearLayoutManager(context, RecyclerView.VERTICAL, false));
    }

    private void initViews() {
        View rootView = getActivity().findViewById(R.id.root);
        final Context context = rootView.getContext();
        initList(context);
        final EmojiconsPopup popup = new EmojiconsPopup(rootView, context);
        popup.setSizeForSoftKeyboard();

        mEtMessage.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mBtnSend.setVisibility(s != null && s.length() > 0 ? View.VISIBLE : View.GONE);
            }
        });

        //If the emoji popup is dismissed, change emojiButton to smiley icon
        popup.setOnDismissListener(new PopupWindow.OnDismissListener() {

            @Override
            public void onDismiss() {
                Log.e("TAG", "popup onDismiss");
            }
        });

        //If the text keyboard closes, also dismiss the emoji popup
        popup.setOnSoftKeyboardOpenCloseListener(
                new EmojiconsPopup.OnSoftKeyboardOpenCloseListener() {

                    @Override
                    public void onKeyboardOpen(int keyBoardHeight) {
                        Log.e("TAG", "onKeyboardOpen");
                    }

                    @Override
                    public void onKeyboardClose() {
                        Log.e("TAG", "onKeyboardClose");
                        if (popup.isShowing())
                            popup.dismiss();
                    }
                });

        //On emoji clicked, add it to edittext
        popup.setOnEmojiconClickedListener(new EmojiconGridView.OnEmojiconClickedListener() {

            @Override
            public void onEmojiconClicked(Emojicon emojicon) {
                Log.e("TAG", "onEmojiconClicked");
                if (mEtMessage == null || emojicon == null) {
                    return;
                }

                int start = mEtMessage.getSelectionStart();
                int end = mEtMessage.getSelectionEnd();
                if (start < 0) {
                    mEtMessage.append(emojicon.getEmoji());
                } else {
                    mEtMessage.getText().replace(Math.min(start, end),
                            Math.max(start, end), emojicon.getEmoji(), 0,
                            emojicon.getEmoji().length());
                }
            }
        });

        //On backspace clicked, emulate the KEYCODE_DEL key event
        popup.setOnEmojiconBackspaceClickedListener(
                new EmojiconsPopup.OnEmojiconBackspaceClickedListener() {

                    @Override
                    public void onEmojiconBackspaceClicked(View v) {
                        Log.e("TAG", "onEmojiconBackspaceClicked");
                        KeyEvent event = new KeyEvent(
                                0, 0, 0, KeyEvent.KEYCODE_DEL, 0, 0, 0, 0,
                                KeyEvent.KEYCODE_ENDCALL);
                        mEtMessage.dispatchKeyEvent(event);
                    }
                });

        // To toggle between text keyboard and emoji keyboard keyboard(Popup)
        mBtnEmoji.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Log.e("TAG", "emoji button clicked");

                //If popup is not showing => emoji keyboard is not visible, we need to show it
                if (!popup.isShowing()) {
                    mBtnEmoji.setImageResource(R.drawable.ic_hardware_keyboard);
                    Log.e("TAG", "popup is not showing");
                    //If keyboard is visible, simply show the emoji popup
                    if (popup.isKeyBoardOpen()) {
                        Log.e("TAG", "keyboard is shown, opening a popup");
                        popup.showAtBottom();
                    }

                    //else, open the text keyboard first and immediately after that show the
                    // emoji popup
                    else {
                        Log.e("TAG", "keyboard is not shown, opening a keyboard and a popup");
                        mEtMessage.setFocusableInTouchMode(true);
                        mEtMessage.requestFocus();
                        popup.showAtBottomPending();
                        final InputMethodManager inputMethodManager
                                = (InputMethodManager) context.getSystemService(
                                Context.INPUT_METHOD_SERVICE);
                        inputMethodManager.showSoftInput(mEtMessage,
                                InputMethodManager.SHOW_FORCED);
                    }
                }

                //If popup is showing, simply dismiss it to show the underlying text keyboard
                else {
                    Log.e("TAG", "popup is showing, dismissing now");
                    popup.dismiss();
                    mBtnEmoji.setImageResource(R.drawable.ic_action_social_mood);
                }
            }
        });

    }

    private void loadConversation() {
        mConversation = mRealm.where(Conversation.class).equalTo("id", mConversationId).findFirst();
        if (mConversation == null) {
            return;
        }
        mIsGroupChat = ConversationUtils.isConversationAGroupChat(mConversation);
        if (!mIsGroupChat) {
            mAnotherUser = mUserProvider.getUser(mConversation.getId());
        }
    }

    private String getToolbarTitle() {
        if (mConversation == null) {
            return null;
        }
        if (ConversationUtils.isConversationAGroupChat(mConversation)) {
            return mConversation.getMessage().getTitle();
        } else {
            return mAnotherUser.getFirstName() + " " + mAnotherUser.getLastName();
        }
    }

    private void initToolbar() {
        ((AbstractActivity) getActivity()).setSupportActionBar(mToolbar);
        mToolbar.setTitleTextColor(Color.WHITE);
        mToolbar.setOnMenuItemClickListener(this);
        ((AbstractActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        (getActivity()).setTitle(getToolbarTitle());

    }

    private void loadLastMessages() {
        // TODO refactor and make pagination

        // TODO
//        mWebRequestManager.getChatHistory(0, 18, mIsGroupChat ? 0 : mConversationId,
//                mIsGroupChat ? mConversationId : 0, 0, false, new WebCallback<ListOfMessages>() {
//                    @Override
//                    public void onUserLoaded(ListOfMessages response) {
//                        Log.e("TAG", "messages received: " + response.getMessages().size());
//                    }
//
//                    @Override
//                    public void onResponseFailed(VKError e) {
//                        Log.e("TAG", "error");
//                    }
//                });
    }
}
