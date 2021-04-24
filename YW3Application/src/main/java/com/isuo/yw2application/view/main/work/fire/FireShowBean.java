package com.isuo.yw2application.view.main.work.fire;

import android.support.annotation.Nullable;

import com.isuo.yw2application.mode.fire.FireBean;
import com.isuo.yw2application.mode.fire.FireListBean;

public class FireShowBean {

   public FireShowBean(int level, int childCount, long id, int count, String name, boolean open, FireListBean fireListBean, @Nullable FireBean fireBean) {
      this.level = level;
      this.childCount = childCount;
      this.id = id;
      this.count = count;
      this.name = name;
      this.open = open;
      this.fireListBean = fireListBean;
      this.fireBean = fireBean;
   }

   public FireShowBean() {
   }

   private  int level;
   private  int childCount;
   private  long id;
   private  int count;
   private String name;
   private boolean open;
   private FireListBean fireListBean;
   @Nullable
   private FireBean fireBean;

   public int getLevel() {
      return level;
   }

   public void setLevel(int level) {
      this.level = level;
   }

   public int getChildCount() {
      return childCount;
   }

   public void setChildCount(int childCount) {
      this.childCount = childCount;
   }

   public long getId() {
      return id;
   }

   public void setId(long id) {
      this.id = id;
   }

   public int getCount() {
      return count;
   }

   public void setCount(int count) {
      this.count = count;
   }

   public String getName() {
      return name;
   }

   public void setName(String name) {
      this.name = name;
   }

   public boolean isOpen() {
      return open;
   }

   public void setOpen(boolean open) {
      this.open = open;
   }

   public FireListBean getFireListBean() {
      return fireListBean;
   }

   public void setFireListBean(FireListBean fireListBean) {
      this.fireListBean = fireListBean;
   }

   @Nullable
   public FireBean getFireBean() {
      return fireBean;
   }

   public void setFireBean(@Nullable FireBean fireBean) {
      this.fireBean = fireBean;
   }
}
