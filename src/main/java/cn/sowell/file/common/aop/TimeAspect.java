package cn.sowell.file.common.aop;

//import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
//import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

//import cn.sowell.file.common.model.BaseModelCriteria;

/**
 * 往dao层的insert和update方法中织入操作时间
 * 
 * @author Xiaojie.Xu
 */
@Aspect
@Component
class TimeAspect {

//	@Before("execution(* cn.sowell.file.modules.service..*.insert*(..))")
//	public void insertTime(JoinPoint point) {
//		Object o = point.getArgs()[0];
//		if (o != null && o instanceof BaseModelCriteria) {
//			BaseModelCriteria criteria = (BaseModelCriteria) o;
//			long time = System.currentTimeMillis();
//			criteria.setCreateTime(time);
//			criteria.setUpdateTime(time);
//		}
//	}
//
//	@Before("execution(* cn.sowell.file.modules.service..*.update*(..))")
//	public void updateTime(JoinPoint point) {
//		Object o = point.getArgs()[0];
//		if (o != null && o instanceof BaseModelCriteria) {
//			BaseModelCriteria criteria = (BaseModelCriteria) o;
//			criteria.setUpdateTime(System.currentTimeMillis());
//		}
//	}
}
