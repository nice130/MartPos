package services.sales;

import java.sql.SQLException;
import java.util.ArrayList;

import beans.Order;

public class DataAccessObject extends controller.DataAccessObject {

	public DataAccessObject() {
		this.dbConnect();
	}

	boolean isGoodsCode(Order order) {
		boolean result = false;
		String query = "SELECT COUNT(*) AS CNT FROM GO WHERE GO_CODE = ?";
		try {
			pstmt = connect.prepareStatement(query);
			pstmt.setNString(1, order.getGoodsCode());
			rs = pstmt.executeQuery();

			while(rs.next()) {
				result = isCheck(rs.getInt("CNT"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}

	boolean insOrders(Order order) {
		String query = (order.getOrderDate()!=null)?
				"INSERT INTO ORD(ORD_EMCODE, ORD_CUCODE, ORD_DATE, ORD_STATE) VALUES(?, ?, TO_DATE(?, 'YYYYMMDDHH24MISS'), ?)"
				:"INSERT INTO ORD(ORD_EMCODE, ORD_CUCODE, ORD_DATE, ORD_STATE) VALUES(?, ?, DEFAULT, ?)";
		boolean resurt = false;
		
		try {
			pstmt = connect.prepareStatement(query);
			pstmt.setNString(1, order.getEmpCode());
			pstmt.setNString(2, order.getCuCode());
			if(order.getOrderDate()!=null) {
				pstmt.setNString(3, order.getOrderDate());
				pstmt.setNString(4, order.getOrderState());
			} else {
				pstmt.setNString(3, order.getOrderState());
			}
			resurt = isCheck(pstmt.executeUpdate());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return resurt;
	}
	
	boolean updOrderState(Order order) {
		String query = "UPDATE ORD SET ORD_STATE = ? WHERE ORD_EMCODE = ? AND ORD_CUCODE = ? AND ORD_DATE = TO_DATE(?, 'YYYYMMDDHH24MISS')";
		boolean result = false;

		try {
			pstmt = connect.prepareStatement(query);
			pstmt.setNString(1, order.getOrderState());
			pstmt.setNString(2, order.getEmpCode());
			pstmt.setNString(3, order.getCuCode());
			pstmt.setNString(4, order.getOrderDate());

			result = (pstmt.executeUpdate()==0)? false:true ;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return result;
	}
	
	boolean updOrderDetail(Order order) {
		boolean result = false;
		String query = "UPDATE ODT SET ODT_ORDCUCODE=? WHERE ODT_ORDEMCODE =? AND ODT_ORDDATE=TO_DATE(?, 'YYYY-MM-DD HH24:MI:SS') AND ODT_ORDCUCODE='9999       ' AND ODT_GOCODE=?";
		try {
			pstmt = connect.prepareStatement(query);
			pstmt.setNString(1, order.getCuCode());
			pstmt.setNString(2, order.getEmpCode());
			pstmt.setNString(3, order.getOrderDate());
			pstmt.setNString(4, order.getGoodsCode());
			
			result = (pstmt.executeUpdate()==0)? false:true ;
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return result;
	}

	boolean updGoods(Order order) {
		boolean result = false;
		String query = "UPDATE GO SET GO_STOCKS = GO_STOCKS - ? WHERE GO_CODE = ?";
		System.out.println(order.getGoodsCode());
		System.out.println(order.getQuantity());
		try {
			pstmt = connect.prepareStatement(query);
			pstmt.setInt(1, order.getQuantity());
			pstmt.setNString(2, order.getGoodsCode());
			
			result = (pstmt.executeUpdate()==0)? false:true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}

	void selOrderDate(Order order) {
		String query = "SELECT TO_CHAR(MAX(ORD_DATE), 'YYYYMMDDHH24MISS') AS ORDERDATE FROM ORD WHERE ORD_EMCODE=? AND ORD_CUCODE=?";

		try {
			pstmt = connect.prepareStatement(query);
			pstmt.setNString(1, order.getEmpCode());
			pstmt.setNString(2, order.getCuCode());
			rs = pstmt.executeQuery();

			while(rs.next()) {
				order.setOrderDate(rs.getNString("ORDERDATE"));
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	boolean insOrderDetail(Order order) {
		boolean result = false;
		String query = "INSERT INTO ODT(ODT_ORDEMCODE, ODT_ORDCUCODE, ODT_ORDDATE, ODT_GOCODE, ODT_QUANTITY) VALUES(?, ?, TO_DATE(?, 'YYYYMMDDHH24MISS'), ?, ?)";

		try {
			pstmt = connect.prepareStatement(query);
			pstmt.setNString(1, order.getEmpCode());
			pstmt.setNString(2, order.getCuCode());
			pstmt.setNString(3, order.getOrderDate());
			pstmt.setNString(4, order.getGoodsCode());
			pstmt.setInt(5, order.getQuantity());

			result = isCheck(pstmt.executeUpdate());
		} catch (SQLException e) {
			query = "UPDATE ODT SET ODT_QUANTITY=ODT_QUANTITY+1 WHERE ODT_ORDEMCODE=? AND ODT_ORDCUCODE =? AND ODT_ORDDATE=TO_DATE(?, 'YYYYMMDDHH24MISS') AND ODT_GOCODE = ?";

			try {
				pstmt = connect.prepareStatement(query);
				pstmt.setNString(1, order.getEmpCode());
				pstmt.setNString(2, order.getCuCode());
				pstmt.setNString(3, order.getOrderDate());
				pstmt.setNString(4, order.getGoodsCode());

				result = isCheck(pstmt.executeUpdate());

			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}

		return result;
	}

	ArrayList<Order> selOrderDetail(Order order) {
		String query = "SELECT EMCODE, CUCODE, TO_CHAR(ORDDATE,'YYYYMMDDHH24MISS')AS ORDDATE,GOCODE,GONAME,QUANTITY,PRICE,TOTAL FROM SALES WHERE EMCODE=? AND CUCODE=? AND ORDDATE=TO_DATE(?, 'YYYYMMDDHH24MISS')";
		ArrayList<Order> list = new ArrayList<Order>();

		try {
			pstmt = connect.prepareStatement(query);
			pstmt.setNString(1, order.getEmpCode());
			pstmt.setNString(2, order.getCuCode());
			pstmt.setNString(3, order.getOrderDate());
			rs = pstmt.executeQuery();

			while(rs.next()) {
				Order od = new Order();
				od.setEmpCode(rs.getNString("EMCODE"));
				od.setCuCode(rs.getNString("CUCODE"));
				od.setOrderDate(rs.getNString("ORDDATE"));
				od.setGoodsCode(rs.getNString("GOCODE"));
				od.setGoodsName(rs.getNString("GONAME"));
				od.setGoodsPrice(rs.getInt("PRICE"));
				od.setQuantity(rs.getInt("QUANTITY"));
				od.setAmount(rs.getInt("TOTAL"));
				od.setOrderCode(od.getEmpCode()+od.getOrderDate()+od.getCuCode());
				list.add(od);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}

	boolean delOrderDetail(Order order) {
		boolean result = false;
		String query = "DELETE FROM ODT WHERE ODT_GOCODE = ? AND ODT_ORDEMCODE = ? AND ODT_ORDCUCODE = ? AND ODT_ORDDATE = TO_DATE(?, 'YYYYMMDDHH24MISS')";

		try {
			
			pstmt = connect.prepareStatement(query);
			
			pstmt.setNString(1, order.getGoodsCode()); //posmain에서 넘어온 gocode가 넘어올 수 있도록 파라미터 설정
			pstmt.setNString(2, order.getEmpCode());
			pstmt.setNString(3, order.getCuCode());
			pstmt.setNString(4, order.getOrderDate());

			result = isCheck(pstmt.executeUpdate());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}

	boolean delOrders(Order order) {
		boolean result = false;
		String query = "DELETE FROM ORD WHERE ORD_EMCODE = ? AND ORD_CUCODE = '9999       ' AND ORD_DATE = TO_DATE(?, 'YYYYMMDDHH24MISS')";
		
		try {
			pstmt = connect.prepareStatement(query);
			pstmt.setNString(1, order.getEmpCode());
			pstmt.setNString(2, order.getOrderDate());

			result = isCheck(pstmt.executeUpdate());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}
}

//pstmt에 담고 익스큐트쿼리로 출발시킨다음 rs에 담는다.
//컬럼명은 대소문자 가리지않음
