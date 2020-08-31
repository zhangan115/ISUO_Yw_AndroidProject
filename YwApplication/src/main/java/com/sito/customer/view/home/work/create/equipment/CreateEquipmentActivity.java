package com.sito.customer.view.home.work.create.equipment;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.sito.customer.R;
import com.sito.customer.app.CustomerApp;
import com.sito.customer.common.ConstantStr;
import com.sito.customer.mode.bean.equip.EquipmentBean;
import com.sito.customer.view.BaseActivity;
import com.sito.customer.view.home.discover.equiplist.EquipListActivity;
import com.sito.library.utils.ActivityUtils;

/**
 * 创建设备/修改设备信息
 * Created by zhangan on 2017/9/30.
 */

public class CreateEquipmentActivity extends BaseActivity {

    private EquipmentBean equipmentBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        equipmentBean = getIntent().getParcelableExtra(ConstantStr.KEY_BUNDLE_OBJECT);
        long roomId = getIntent().getLongExtra(ConstantStr.KEY_BUNDLE_LONG, 0);
        String roomName = getIntent().getStringExtra(ConstantStr.KEY_BUNDLE_STR);
        String title;
        if (equipmentBean == null) {
            title = getString(R.string.toolbar_title_create_equip);
        } else {
            title = getString(R.string.toolbar_title_equip_info);
        }
        setLayoutAndToolbar(R.layout.activity_container_toolbar, title);
        CreateEquipFragment fragment = (CreateEquipFragment) getFragmentManager().findFragmentById(R.id.frame_container);
        if (fragment == null) {
            fragment = CreateEquipFragment.newInstance(equipmentBean, roomId, roomName);
            ActivityUtils.addFragmentToActivity(getFragmentManager(), fragment, R.id.frame_container);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_equipment, menu);
        return equipmentBean == null;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu) {
            Intent intent = new Intent(this, EquipListActivity.class);
            intent.putExtra(ConstantStr.KEY_BUNDLE_BOOLEAN, true);
            startActivity(intent);
        }
        return true;
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slideinleft, R.anim.slideoutright);
    }

    @Override
    public void toolBarClick() {
        CustomerApp.getInstance().hideSoftKeyBoard(this);
        super.toolBarClick();
    }
}
