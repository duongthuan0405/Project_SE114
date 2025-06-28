package com.example.tqt_quiz.data.interactor;

import android.content.Context;
import android.util.Log;

import com.example.tqt_quiz.data.repository.RoleManager;
import com.example.tqt_quiz.data.repository.token.RetrofitClient;
import com.example.tqt_quiz.data.repository.token.TokenManager;
import com.example.tqt_quiz.domain.APIService.AccountInfoService;
import com.example.tqt_quiz.domain.dto.AccountInfo;
import com.example.tqt_quiz.domain.interactor.IAccountInteractor;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AccountInteractorIMP implements IAccountInteractor
{

    @Override
    public void getAccountInfoById(String account_id, Context context, GetAccountInfoCallBack callBack)
    {
        TokenManager tokenManager = new TokenManager(context);
        AccountInfoService service = RetrofitClient.GetClient(tokenManager).create(AccountInfoService.class);
        Call<AccountInfo> call = service.getAccountInfoById(account_id);
        call.enqueue(new Callback<AccountInfo>() {
            @Override
            public void onResponse(Call<AccountInfo> call, Response<AccountInfo> response) {
                if(response.isSuccessful() && response.body() != null)
                {
                    callBack.onSuccess(response.body());
                }
                else
                {
                    String rawJson = "";
                    String msg = "";
                    try {
                        rawJson=response.errorBody().string();
                        JSONObject obj=new JSONObject(rawJson);
                        msg= obj.optString("message");

                        if(response.code() == 404)
                        {
                            callBack.onFailureByNotExistAccount(msg);
                        }
                        else
                        {
                            callBack.onFailureByServerError(msg);
                        }
                    }
                    catch (Exception e)
                    {
                        if(response.code() == 401)
                        {
                            callBack.onFailureByExpiredToken("");
                        }
                        else if(response.code() == 403)
                        {
                            callBack.onFailureByUnAcepptedRole("");
                        }
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<AccountInfo> call, Throwable t) {
                callBack.onFailureByCannotSendToServer("Không thể kết nối đến server");
            }
        });
    }

    @Override
    public void getAccountInfoMySelf(Context context, GetAccountInfoCallBack callBack)
    {

        TokenManager tokenManager = new TokenManager(context);
        AccountInfoService service = RetrofitClient.GetClient(tokenManager).create(AccountInfoService.class);
        Call<AccountInfo> call = service.getAccountMySelf();
        call.enqueue(new Callback<AccountInfo>() {
            @Override
            public void onResponse(Call<AccountInfo> call, Response<AccountInfo> response) {
                if(response.isSuccessful() && response.body() != null)
                {
                    callBack.onSuccess(response.body());
                }
                else
                {
                    try {
                        String rawJson = "";
                        String msg = "";
                        rawJson = response.errorBody().string();
                        JSONObject obj = new JSONObject(rawJson);
                        msg = obj.optString("message");

                        if(response.code() == 404)
                        {
                            callBack.onFailureByNotExistAccount(msg);
                        }
                        else
                        {
                            callBack.onFailureByServerError(msg);
                        }
                    }
                    catch (Exception e)
                    {

                        if(response.code() == 401)
                        {
                            callBack.onFailureByExpiredToken("");
                        }
                        else if(response.code() == 403)
                        {
                            callBack.onFailureByUnAcepptedRole("");
                            e.printStackTrace();
                        }
                    }

                }

            }

            @Override
            public void onFailure(Call<AccountInfo> call, Throwable t) {
                callBack.onFailureByCannotSendToServer("Không thể kết nối đến server");
            }
        });
    }

    @Override
    public void logOut(Context context) {
        TokenManager tokenManager = new TokenManager(context);
        tokenManager.ClearToken();
        RoleManager roleManager = new RoleManager(context);
        roleManager.ClearRole();
    }
}
