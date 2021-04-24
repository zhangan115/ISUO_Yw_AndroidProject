package com.isuo.yw2application.view.main.work.fire;

import android.support.annotation.Nullable;

import com.isuo.yw2application.mode.fire.FireBean;

public class FireShowBean {
   public FireShowBean(int id,int level, int count, String name, @Nullable FireBean fireBean) {
      this.id = id;
      this.level = level;
      this.count = count;
      this.name = name;
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
   @Nullable
   private FireBean fireBean;

   public int getLevel() {
      return level;
   }

   public void setLevel(int level) {
      this.level = level;
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

   @Nullable
   public FireBean getFireBean() {
      return fireBean;
   }

   public boolean isOpen() {
      return open;
   }

   public void setOpen(boolean open) {
      this.open = open;
   }

   public long getId() {
      return id;
   }

   public void setId(long id) {
      this.id = id;
   }

   public int getChildCount() {
      return childCount;
   }

   public void setChildCount(int childCount) {
      this.childCount = childCount;
   }

   public void setFireBean(@Nullable FireBean fireBean) {
      this.fireBean = fireBean;
   }
}
