package code;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.gui.it.code.CodeApplication;
import com.gui.it.code.service.base.CodeService;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = CodeApplication.class)
public class SpringTest {
	
	@Autowired
	CodeService codeService;
	
	@Test
	public void test() {
		codeService.generetor("user", "role");
	}

}
