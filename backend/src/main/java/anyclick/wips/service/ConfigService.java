package anyclick.wips.service;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import anyclick.wips.repository.ConfigRepository;

@Service
@Transactional
public class ConfigService {

	@Autowired
	ConfigRepository repo;

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	public Map getConfig() {
		return repo.getConfig();
	}
}
