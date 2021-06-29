package com.isuo.yw2application.common;

/**
 * 广播常量
 * Created by zhangan on 2017/11/17.
 */

public interface BroadcastAction {

    String CLEAN_ALL_DATA = "action_clean_all_message";//清除了所有消息
    String MESSAGE_UN_READ_STATE = "action_un_read_state";
    String MESSAGE_NEW_MESSAGE = "action_new_message";

    String AUTO_UPLOAD_DATA = "auto_upload_data";
    String AUTO_REFRESH_UI = "auto_refresh_ui";
    String AUTO_SAVE_DATA = "auto_save_dataa";

    String USER_PHOTO_UPDATE = "user_photo_update";
}
