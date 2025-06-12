package com.example.tqt_quiz.data.interactor;

import com.example.tqt_quiz.data.repository.MainRepositoryImp;
import com.example.tqt_quiz.domain.interactor.IMainInteractor;
import com.example.tqt_quiz.domain.APIService.IMainLogic;
import com.example.tqt_quiz.domain.repository.IMainRepository;
public class MainInteractorImp implements IMainInteractor
{
    IMainLogic logic = null;
    IMainRepository repo = null;

    public MainInteractorImp()
    {
        repo = new MainRepositoryImp();
    }
    @Override
    public String getDataToShowView() {
        return repo.getData();
    }
}
