package com.example.myfirstapp.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.example.myfirstapp.database.PiggybankRepository;
import com.example.myfirstapp.model.Tag;

import java.util.List;

public class TagViewModel extends AndroidViewModel {

    private PiggybankRepository repository;
    private LiveData<List<Tag>> allTagsByDateAdded;
    private LiveData<List<Tag>> allTagsByTimeClicked;

    public TagViewModel(@NonNull Application application) {
        super(application);

        repository = new PiggybankRepository(application);
        allTagsByDateAdded = repository.getAllTagsByDateAdded();
        allTagsByTimeClicked = repository.getAllTagsByTimeClicked();
    }

    public void insert(Tag tag)
    {
        repository.insertTag(tag);
    }

    public void update(Tag tag)
    {
        repository.updateTag(tag);
    }

    public void delete(Tag tag)
    {
        repository.deleteTag(tag);
    }

    public LiveData<List<Tag>> getAllTagsByDateAdded()
    {
        return allTagsByDateAdded;
    }

    public LiveData<List<Tag>> getAllTagsByTimeClicked()
    {
        return allTagsByTimeClicked;
    }
}
