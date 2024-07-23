/** 
 * PROJECT  : 재능기부 프로젝트
 * NAME  :  TalentDonationProjectService.java
 * DESC  :  재능 기부 프로젝트를 저장, 수정, 삭제, 검색하는 서비스 로직
 * 
 * @author  
 * @version 1.0
*/

package probono.service;

import java.util.ArrayList;
import java.util.Optional;
import java.util.stream.Collectors;

import probono.model.dto.Beneficiary;
import probono.model.dto.Donator;
import probono.model.dto.TalentDonationProject;


public class TalentDonationProjectService {

	// singleton design pattern
	private static TalentDonationProjectService instance = new TalentDonationProjectService();

	/**   
	 * 진행중인 Project를 저장
	 */
	private ArrayList<TalentDonationProject> donationProjectList = new ArrayList<TalentDonationProject>();

	private TalentDonationProjectService() {}

	public static TalentDonationProjectService getInstance() {
		return instance;
	}

	/**
	 * 모든 Project 검색
	 * 
	 * @return 모든 Project
	 */
	public ArrayList<TalentDonationProject> getDonationProjectsList() {
		return donationProjectList;
	}

	// TO DO - 구현 및 완성해야 할 job
	/**
	 * Project 이름으로 검색 - 객체된 Project 반환하기
	 * 
	 * @param projectName 프로젝트 이름
	 * @return TalentDonationProject 검색된 프로젝트
	 */
	public TalentDonationProject getDonationProject(String projectName) {
		// stream api 수정
		return donationProjectList.stream().filter(project -> project!=null && project.getTalentDonationProjectName()
.equals(projectName)).findFirst().orElse(null);
		}


	/**
	 * 새로운 Project 추가
	 * 
	 * @param project 저장하고자 하는 새로운 프로젝트
	 */
	
	/* Controller의 메소드 
	 * 	public void donationProjectInsert(TalentDonationProject project){}
	 * */
    public void donationProjectInsert(TalentDonationProject project) throws Exception {
        if (donationProjectList.stream()
                               .anyMatch(p -> p.getTalentDonationProjectName().equals(project.getTalentDonationProjectName()))) {
            throw new Exception("해당 project명은 이미 존재합니다. 재 확인하세요");
        }
        donationProjectList.add(project);
    }

	/**
	 * Project의 기부자 수정 - 프로젝트 명으로 검색해서 해당 프로젝트의 기부자 수정
	 * 
	 * @param projectName 프로젝트 이름
	 * @param people      기부자
	 */
    // stream api 수정
    public void donationProjectUpdate(String projectName, Donator people) throws Exception {
        Optional<TalentDonationProject> projectOpt = donationProjectList.stream()
                                                                        .filter(project -> project != null && project.getTalentDonationProjectName().equals(projectName))
                                                                        .findFirst();
        if (projectOpt.isPresent()) {
            TalentDonationProject project = projectOpt.get();
            if (people != null) {
                project.setProjectDonator(people);
            } else {
                throw new Exception("프로젝트 이름은 있으나 기부자 정보 누락 재확인 하세요");
            }
        } else {
            throw new Exception("프로젝트 이름과 기부자 정보 재 확인 하세요");
        }
    }


	/**
	 * Project의 수혜자 수정 - 프로젝트 명으로 검색해서 해당 프로젝트의 수혜자 수정
	 * 
	 * @param projectName 프로젝트 이름
	 * @param people      수혜자
	 */
    // stream api 수정
    public void beneficiaryProjectUpdate(String projectName, Beneficiary people) throws Exception {
        Optional<TalentDonationProject> projectOpt = donationProjectList.stream()
                                                                        .filter(project -> project != null && project.getTalentDonationProjectName().equals(projectName))
                                                                        .findFirst();
        if (projectOpt.isPresent()) {
            TalentDonationProject project = projectOpt.get();
            project.setProjectBeneficiary(people);
        } else {
            throw new Exception("프로젝트 이름 재 확인 하세요");
        }
    }


	/**
	 * Project 삭제 - 프로젝트 명으로 해당 프로젝트 삭제
	 * 
	 * @param projectName 삭제하고자 하는 프로젝트 이름
	 */
    public void donationProjectDelete(String projectName) {
        donationProjectList = donationProjectList.stream()
                                                 .filter(project -> !project.getTalentDonationProjectName().equals(projectName))
                                                 .collect(Collectors.toCollection(ArrayList::new));
    }
}
