/** 
4 * PROJECT  : 재능기부 프로젝트
 * NAME  :  StartView.java
 * DESC  : 실행 클래스
 * 		     기부자, 수혜자, 재능기부 종류 및 실제 진행되는 프로젝트 생성 및 CRUD 로직을 test하는 클래스
 * 
 * @author  
 * @version 1.0
*/

package probono.view;

import java.util.stream.Stream;

import probono.controller.TalentDonationProjectController;
import probono.model.dto.Beneficiary;
import probono.model.dto.Donator;
import probono.model.dto.TalentDonationProject;
import probono.model.dto.TalentDonationType;


public class StartView {

	public static void main(String[] args) {

		// 기부자 정보 - 사번, 사원명, 이메일, 기부하기를 희망하는 재능기부 종류
		Donator donator1 = new Donator(7369, "김의사", "kimdoc@company.com", "슈바이처 프로젝트");
		Donator donator2 = new Donator(7156, "신예능", "shin@company.com", "오드리햅번 프로젝트");
		Donator donator3 = new Donator(8012, "이레사", "lee@company.com", "마더테레사 프로젝트");
		Donator donator4 = new Donator(7777, "박메너", "parkdoc@company.com", "슈바이처 프로젝트");

		// 수혜자 정보 - 수혜자 번호, 이름, 연락처, 기부 받기를 희망하는 재능기부 종류
		Beneficiary beneficiary1 = new Beneficiary(100, "김연약", "010-111-1111", "슈바이처 프로젝트");
		Beneficiary beneficiary2 = new Beneficiary(101, "박아트", "010-222-2222", "오드리햅번 프로젝트");
		Beneficiary beneficiary3 = new Beneficiary(105, "이건강", "010-555-5555", "마더테레사 프로젝트");
		Beneficiary beneficiary4 = new Beneficiary(103, "맘아픔", "010-333-3333", "슈바이처 프로젝트");

		// 재능기부 종류 - 구분, 활동영역, 활동예시
		TalentDonationType schweitzer = new TalentDonationType("슈바이처 프로젝트", "의료, 보건, 건강과 관련된 분야",
				"의사, 한의사, 수의사, 스포츠 마사지, 수지침, 이혈, 발마사지 등 의료 활동이나 의료 활동을 위한 후원, 보건, 의료 활동 보조, 대체의학 요번, 보건위생, 응급 처치등");
		TalentDonationType audreyHepbun = new TalentDonationType("오드리햅번 프로젝트", "문화, 예술 관련 분야",
				"예술가, 문화관련 프로그램 제공, 전시ㆍ관람 등 기회제공, 사진, 영상, 디자인, 메이크업, 마술, 모델, 활용 캠페인 등");
		TalentDonationType motherTeresa = new TalentDonationType("마더테레사 프로젝트", "저소득층 및 사회복지 분야",
				"사회복지 관련 시설기관 봉사 및 후원, 독거노인 돌봄, 그룸혹, 쉼터 지원등");
		TalentDonationType daddyLongLegs = new TalentDonationType("키다리아저씨 프로젝트", "멘토링, 상담, 교육, 결연 분야",
				"결연, 상담, 멘토, 독서ㆍ학습지도 및 교육기회 제공, 장학지원, 심리상담 등 멘토링, 상담, 교육, 결연분야");
		TalentDonationType heracles = new TalentDonationType("헤라클레스 프로젝트", "체육, 기능,기술 관련 분야",
				"체육활동 및 교육, 집수리 봉사, 운전,"
				+ " 배송, 엔지니어링, 기술 제공 및 자문등");

		// 기부자, 수혜자가 매핑된 실제 진행되는 "재능 기부 프로젝트"
		// 프로젝트명, 기부자, 수혜자, 재능기부종류, 시작일, 종료일, 재능기부 실제 내용
		TalentDonationProject schweitzerProject 
		= new TalentDonationProject("01슈바이처", donator1, beneficiary1, schweitzer, "2024-11-31", "2024-12-03", 	"아토피 무상 치료");
							        		
		TalentDonationProject audreyHepbunPorject = new TalentDonationProject("02오드리햅번", donator2, beneficiary2,
				audreyHepbun, "2024-11-31", "2024-12-03", "예술가와의 만남");
		TalentDonationProject motherTeresaProject = new TalentDonationProject("03마더테레사", donator3, beneficiary3,
				motherTeresa, "2024-11-31", "2024-12-03", "독거 노인 식사 제공");
		  

		// 데이터 구성 후 서비스 로직 실행
		TalentDonationProjectController controller = TalentDonationProjectController.getInstance();
		

		System.out.println("*** 01. Project 생성 ***");
		// 저장 시도시 이미 존재할 경우 예외 발생, 미 존재시 정상 저장 실행

		// stream api 수정
		Stream.of(schweitzerProject, audreyHepbunPorject, motherTeresaProject)
        .forEach(controller::donationProjectInsert);
		
		System.out.println("\n*** 02. 모든 Project 검색 ***".toString());
		controller.getDonationProjectsList();
		
		

		System.out.println("\n*** 03. '01슈바이처' Project 검색 ***");
		controller.getDonationProject("01슈바이처");
		

		// 재능 기부자 변경하기
		System.out.println("\n*** 04. '01슈바이처' Project의 기부자 변경(수정) 후 해당 Project 검색 ***");
		controller.donationProjectUpdate("01슈바이처", donator4);
		controller.getDonationProject("01슈바이처");
		
		
		//재능 기부자 삭제하기 
		System.out.println("\n*** 05. '01슈바이처' Project 삭제 후 삭제한 Project 존재 여부 검색 ***");
		controller.donationProjectDelete("01슈바이처");
		controller.getDonationProject("01슈바이처");
		
	}
}
