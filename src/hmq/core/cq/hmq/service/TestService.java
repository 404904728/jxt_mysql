package core.cq.hmq.service;

import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import core.cq.hmq.dao.DaoExtends;

/**
 * 在方法上加 @test 则可以测试
 * 
 * @author cqmonster
 * 
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:test-spring.xml" })
@Transactional(readOnly = true)
public class TestService extends DaoExtends {

}
