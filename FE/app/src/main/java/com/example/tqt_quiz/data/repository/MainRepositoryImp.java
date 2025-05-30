package com.example.tqt_quiz.data.repository;

import com.example.tqt_quiz.domain.repository.IMainRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class MainRepositoryImp implements IMainRepository
{
    @Override
    public String getData() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm:ss"));
    }
}
