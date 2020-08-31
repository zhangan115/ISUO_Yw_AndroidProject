package com.sito.customer.mode;

import android.content.Context;

import com.sito.customer.mode.work.DaggerWorkRepositoryComponent;
import com.sito.customer.mode.work.WorkRepository;
import com.sito.customer.view.ApplicationModule;

/**
 * 管理mode reposition
 * Created by zhangan on 2018/3/16.
 */

public class Injection {
    private static Injection injection;

    private Injection() {

    }

    public static Injection getInstance() {
        if (injection == null) {
            injection = new Injection();
        }
        return injection;
    }

    private WorkRepository workRepository;

    public WorkRepository getWorkRepository(Context context) {
        if (workRepository == null) {
            workRepository = DaggerWorkRepositoryComponent.builder()
                    .applicationModule(new ApplicationModule(context))
                    .build().getRepository();
        }
        return workRepository;
    }
}
