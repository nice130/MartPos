package services.auth;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import beans.Employee;
import beans.Form;

public class Approach extends controller.Authentication{
	private DataAccessObject dao;

	public Approach(HttpServletRequest req) {
		this.req = req;
		form = new Form();
		session = req.getSession();
	}

	public Form backController(int serviceCode) {
		if(serviceCode == 100) {
			moveIndex();
		} else if (serviceCode == 200) {
			login();
		} else if (serviceCode == 300) {
			logout();
		}

		return form;
	}

	private void moveIndex() {
		String page = "index.jsp";
		boolean redirect = true;
		//scode가 널인경우는 처음로그인이나 로그아웃 후나 똑같기때문에 로그아웃한 정보로 비교해준다. (로그아웃이 널값이면 로그인해주고 널값이 아니면 (값이있으면)로그아웃한거니 인덱스페이지로 가게 해준다)
		if(session.getAttribute("sCode") != null) {
			Employee emp = new Employee();
			emp.setEmCode(session.getAttribute("sCode").toString());

			dao = new DataAccessObject();
			getEmpInfo(emp); 
			dao.dbClose();

			req.setAttribute("empName", emp.getEmName());
			req.setAttribute("lastAccessTime", emp.getLastAccessTime());
			req.setAttribute("empJobCode", emp.getJobCode());

			page="access.jsp"; //리퀘스트에 담긴 값을 access로 전송
			redirect = false;

		}
		form.setPage(page);
		form.setRedirect(redirect);
	}
	private void login() {
		String page = "index.jsp";
		boolean redirect = true;
		if(session.getAttribute("logout") == null) {
			dao = new DataAccessObject();
			Employee emp = new Employee();

			emp.setEmCode(req.getParameterValues("userInfo")[0]);
			emp.setEmPwd(req.getParameterValues("userInfo")[1]);
			emp.setLogType(1);

			dao.setTransaction(false);

			if(isEmployee(emp)) {
				if(isAuth(emp)) {
					getEmpInfo(emp);
					boolean tran = insEmpLog(emp);
					dao.setTransactionResult(tran);

					if(tran) {
						req.setAttribute("empName", emp.getEmName());
						req.setAttribute("lastAccessTime", emp.getLastAccessTime());
						req.setAttribute("empJobCode", emp.getJobCode());
						session.setAttribute("sCode", emp.getEmCode());
						page="access.jsp";
						redirect = false;
					}
				}
			}
			dao.setTransaction(true);
			dao.dbClose();
		} else {
			session.removeAttribute("logout");
		}
		form.setPage(page);
		form.setRedirect(redirect);
	}


	private void logout() {
		//데이터베이스 인서트, 서버에 세션값 죽이기
		Employee emp = new Employee();
		String page = "index.jsp";
		boolean redirect = true;

		if(session.getAttribute("sCode")!=null) {
			//emp.setEmCode(req.getParameter("out"));
			emp.setEmCode(session.getAttribute("sCode").toString());
			//저장된 값을 문자로 저장시켜줘야하니까 투 스트링으로 변환시켜서 저장
			emp.setLogType(-1);

			dao = new DataAccessObject();
			dao.setTransaction(false);

			//dao.setTransactionResult(false);

			if(dao.insEmpLog(emp)) { //인서트가 성공해서 1=true가 오면 실행 실패하면 롤백
				dao.setTransactionResult(true);
			}else {
				dao.setTransactionResult(false);
			}

			dao.setTransaction(true);
			dao.dbClose();

			//서버 로그아웃 표시
			session.removeAttribute("sCode"); //데이터베이스 로그아웃이 실패한 경우에도 세션값은 지워주고 인덱스페이지로 이동해주기로 한다.
			//서버에 세션값이 없으면 데이터베이스에는 로그아웃 저장이 되지 않고 세션값만 죽이고 인덱스페이지로 이동한다.
			session.setAttribute("logout", true); //로그아웃값에 어떤값이든 넣어도 된다.
		}
		//서버에 세션값이 없으면 데이터베이스에는 로그아웃 저장이 되지 않고 세션값만 죽이고 인덱스페이지로 이동한다.
		form.setPage(page);
		form.setRedirect(redirect);
	}

	//사용자 정보 조회 ( id값 확인 )
	protected boolean isEmployee(Employee emp) {
		return dao.isEmployee(emp);
	}

	// 아이디 패스워드 일치 확인
	protected boolean isAuth(Employee emp) {
		return dao.isAuth(emp);
	}

	// 사용자 정보 조회
	protected void getEmpInfo(Employee emp) {
		dao.getEmpInfo(emp);
	}

	// 사용자 로그인 기록 추가
	protected boolean insEmpLog(Employee emp) {
		return dao.insEmpLog(emp);
	}

	// job코드 불러오기

}
