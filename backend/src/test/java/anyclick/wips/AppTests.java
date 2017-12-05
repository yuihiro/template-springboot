package anyclick.wips;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import anyclick.wips.repository.MainRepository;
import anyclick.wips.repository.UserSummaryRepository;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = App.class)
//@WebAppConfiguration
public class AppTests {

	@Autowired
	MainRepository dao;

	@Autowired
	UserSummaryRepository repo;

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	@Test
	public void test() throws Exception {

		SimpleDateFormat sdf = new SimpleDateFormat("yy-dd-mm");

		String start = "2017-03-24";
		String end = "2017-04-24";

		List dates = new ArrayList();
		Calendar cal = Calendar.getInstance();
		cal.setTime(sdf.parse(start));
		while (cal.getTime().before(sdf.parse(end))) {
			cal.add(Calendar.DATE, 1);
			dates.add(sdf.format(cal.getTime()));
		}
		System.out.println(dates);
	}

	//	public void contextLoads() {
	//		int admin_cnt = dao.getUserTypeList().size();
	//		log.debug(admin_cnt + "");
	//		assertTrue(admin_cnt == 10);
	//	}
}
