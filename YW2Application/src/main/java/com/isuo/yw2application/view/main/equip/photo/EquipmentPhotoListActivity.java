package com.isuo.yw2application.view.main.equip.photo;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.isuo.yw2application.R;
import com.isuo.yw2application.common.ConstantStr;
import com.isuo.yw2application.utils.Utils;
import com.isuo.yw2application.view.base.BaseActivity;
import com.isuo.yw2application.view.photo.ViewPagePhotoActivity;
import com.sito.library.adapter.RVAdapter;
import com.sito.library.utils.GlideUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class EquipmentPhotoListActivity extends BaseActivity {
    private String[] photoList;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLayoutAndToolbar(R.layout.activity_equipment_photo_list, "巡检自拍");
        this.photoList = getIntent().getStringArrayExtra(ConstantStr.KEY_BUNDLE_LIST);
        if (this.photoList == null || this.photoList.length == 0) {
            finish();
            return;
        }
        recyclerView = findViewById(R.id.recycleViewId);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 4));
        List<String> resultList = new ArrayList<>(photoList.length);
        Collections.addAll(resultList, photoList);
        RVAdapter<String> adapter = new RVAdapter<String>(recyclerView, resultList, R.layout.item_equipment_list) {
            @Override
            public void showData(ViewHolder vHolder, String data, int position) {
                ImageView imageView = (ImageView) vHolder.getView(R.id.imageView);
                int width = (Utils.getScreenWidth(EquipmentPhotoListActivity.this) - 40) / 4;
                int height = (Utils.getScreenWidth(EquipmentPhotoListActivity.this) - 40) / 3;
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width, height);
                imageView.setLayoutParams(params);
                GlideUtils.ShowImage(EquipmentPhotoListActivity.this,
                        EquipmentPhotoListActivity.this.photoList[position],
                        imageView, R.drawable.picture_default);
            }
        };
        adapter.setOnItemClickListener(new RVAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                ViewPagePhotoActivity.startActivity(EquipmentPhotoListActivity.this, photoList, position);
            }
        });
        recyclerView.setAdapter(adapter);
    }
}
