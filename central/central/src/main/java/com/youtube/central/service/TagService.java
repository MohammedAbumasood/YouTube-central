package com.youtube.central.service;

import com.youtube.central.model.Tag;
import com.youtube.central.repository.TagRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TagService {

    @Autowired
    TagRepo tagRepo;

    public Tag getTagbyName(String name){
        return tagRepo.findByName(name);
    }

    public List<Tag> getAllTagsFromSystem(List<String> tags){
        List<Tag> dbTagList = new ArrayList<>();
        for(int i=0;i< tags.size();i++){
            String tag = tags.get(i);
            //now we need to check is this tag in system pr not
            Tag tagObj = this.getTagbyName(tag);
            if(tagObj == null){
                //Tag with this particular name is not present in system
                // we need to create tag in system
                Tag newTag = new Tag();
                newTag.setName(tag);
                tagRepo.save(newTag);
                dbTagList.add(newTag);
            }else{
                dbTagList.add((tagObj));
            }
        }
        return dbTagList;
    }
}
