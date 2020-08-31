package com.sito.evpro.inspection.view.create.equipment;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.sito.evpro.inspection.R;
import com.sito.evpro.inspection.app.InspectionApp;
import com.sito.evpro.inspection.common.ConstantStr;
import com.sito.evpro.inspection.mode.bean.inspection.EquipmentBean;
import com.sito.evpro.inspection.view.BaseActivity;
import com.sito.evpro.inspection.view.equipment.equiplist.EquipListActivity;
import com.sito.library.utils.ActivityUtils;

import javax.inject.Inject;

/**
 * 创建设备/修改设备信息
 * Created by zhangan on 2017/9/30.
 */

public class CreateEquipmentActivity extends BaseActivity {

    private EquipmentBean equipmentBean;
    @Inject
    CreateEquipPresenter presenter;

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
        }
        DaggerCreateEquipComponent.builder().createRepositoryComponent(InspectionApp.getInstance().getCreateRepositoryComponent())
                .createEquipModule(new CreateEquipModule(fragment))
                .build().inject(this);
        ActivityUtils.addFragmentToActivity(getFragmentManager(), fragment, R.id.frame_container);
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
        InspectionApp.getInstance().hideSoftKeyBoard(this);
        super.toolBarClick();
    }
}
