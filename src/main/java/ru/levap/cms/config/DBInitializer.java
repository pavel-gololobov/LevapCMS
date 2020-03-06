package ru.levap.cms.config;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.DependsOn;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;
import ru.levap.cms.dto.common.UserRoleEnum;
import ru.levap.cms.persistance.model.User;
import ru.levap.cms.persistance.model.UserAuthority;
import ru.levap.cms.persistance.repository.UserRepository;
import ru.levap.cms.service.storage.StorageService;

@Slf4j
@Component
@DependsOn({ "dataSource" })
public class DBInitializer {
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private StorageService storageService;

	@Value("${init-database:false}")
	private boolean initDatabase;

	@PostConstruct
	public void init() {
		log.info("initDatabase {}", initDatabase);
		if (initDatabase) {
			initDatabase();
		}
	}

	void initDatabase() {
		storageService.init();
		
		if(userRepository.count() == 0) {
			log.info("!!!!!!!!!!!! No users. Initializing Database with sample data...");
	
			User admin = new User();
			admin.setActive(true);
			admin.setLogin("admin");
	
			PasswordEncoder encoder = new BCryptPasswordEncoder();
			admin.setPassword(encoder.encode("admin"));
	
			UserAuthority roleUser = new UserAuthority(UserRoleEnum.USER.toRoleName());
			roleUser.setUser(admin);

			UserAuthority roleIntegrator = new UserAuthority(UserRoleEnum.SUPERADMIN.toRoleName());
			roleIntegrator.setUser(admin);
	
			admin.addAuthority(roleUser);
			admin.addAuthority(roleIntegrator);
	
			userRepository.save(admin);
		} else {
			log.info("----> Database already has users");
		}
	}
}
