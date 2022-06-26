package net.javaguides.springboot.springsecurity.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.javaguides.springboot.springsecurity.model.Profile;
import net.javaguides.springboot.springsecurity.repository.ProfileRepository;;

@Service
public class ProfileServiceImpl implements ProfileService {

	@Autowired
	private ProfileRepository daoProd;
	
	@Override
	public void saveProfile(Profile personaa) {
		this.daoProd.save(personaa);		
		
	}

}
