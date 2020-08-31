package com.sito.evpro.inspection.mode.employee;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;

import com.sito.evpro.inspection.api.Api;
import com.sito.evpro.inspection.api.ApiCallBack;
import com.sito.evpro.inspection.common.ConstantStr;
import com.sito.evpro.inspection.mode.Bean;
import com.sito.evpro.inspection.mode.IListCallBack;
import com.sito.evpro.inspection.mode.bean.employee.DepartmentBean;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Observable;
import rx.Subscription;

/**
 * 部门人员
 * Created by zhangan on 2017-07-07.
 */
@Singleton
public class EmployeeRepository implements EmployeeDataSource {

    private SharedPreferences sp;
    private List<DepartmentBean> departmentBeen = null;

    @Inject
    public EmployeeRepository(@NonNull Context context) {
        sp = context.getSharedPreferences(ConstantStr.USER_DATA, Context.MODE_PRIVATE);
    }

    @NonNull
    @Override
    public Subscription getEmployeeList(@NonNull final IListCallBack<DepartmentBean> callBack) {
        if (departmentBeen != null) {
            callBack.onFinish();
            for (int i = 0; i < departmentBeen.size(); i++) {
                if (departmentBeen.get(i).getUserList() == null || departmentBeen.get(i).getUserList().size() == 0) {
                    continue;
                }
                for (int j = 0; j < departmentBeen.get(i).getUserList().size(); j++) {
                    departmentBeen.get(i).getUserList().get(j).setSelect(false);
                }
            }
            callBack.onSuccess(departmentBeen);
            return Observable.just(departmentBeen).subscribe();
        }
        Observable<Bean<List<DepartmentBean>>> observable = Api.createRetrofit().create(Api.Inspection.class)
                .getEmployeeList();
        return new ApiCallBack<List<DepartmentBean>>(observable) {
            @Override
            public void onSuccess(List<DepartmentBean> been) {
                callBack.onFinish();
                if (been == null || been.size() == 0) {
                    callBack.onError("");
                } else {
                    departmentBeen = been;
                    callBack.onSuccess(been);
                }
            }

            @Override
            public void onFail() {
                callBack.onFinish();
                callBack.onError("");
            }
        }.execute().subscribe();
    }

    @Override
    public void cleanCache() {
        departmentBeen = null;
    }
}
