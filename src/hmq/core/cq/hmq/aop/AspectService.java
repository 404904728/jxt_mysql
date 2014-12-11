package core.cq.hmq.aop;

import org.springframework.stereotype.Service;

@Service
public class AspectService  {


	public void doBefore(){
		System.out.println("doBefor------------------");
	}

}
