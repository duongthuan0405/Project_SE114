package com.example.tqt_quiz.data.interactor;

import com.example.tqt_quiz.data.logic.MainLogicImp;
import com.example.tqt_quiz.data.repository.MainRepositoryImp;
import com.example.tqt_quiz.domain.interactor.IMainInteractor;
import com.example.tqt_quiz.domain.logic.IMainLogic;
import com.example.tqt_quiz.domain.repository.IMainRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class MainInteractorImp implements IMainInteractor
{
    IMainLogic logic = null;
    IMainRepository repo = null;

    public MainInteractorImp()
    {
        logic = new MainLogicImp();
        repo = new MainRepositoryImp();
    }
    @Override
    public String getDataToShowView() {
        return repo.getData();
    }
}
