## :hammer_and_wrench: Stram api와 Lambda 표현식을 통한 기존 재능기부 프로젝트의 코드 리펙토링

팀원 : 박현서, 곽병찬
기간 : 2024.07.24 ~ 2024.07.24

Java Refactoring 프로젝트는 Stram API와 Lambda 표현식을 사용하여 기존 재능기부 프로젝트의 코드를 리팩토링하는 것을 목표로 한다. 
StartView.java, TalentDonationProjectController.java, TalentDonationProjectService.java의 코드를 수정하였으며, 기존 코드를 간결하게 만들고 가독성을 높였다.


### StartView.java
```java
 * PROJECT  : 재능기부 프로젝트
 * NAME  :  StartView.java
		
		// 기존 코드
		controller.donationProjectInsert(schweitzerProject) ;
		controller.donationProjectInsert(audreyHepbunPorject);
		controller.donationProjectInsert(motherTeresaProject);

		// stream api 수정
		Stream.of(schweitzerProject, audreyHepbunPorject, motherTeresaProject)
        .forEach(controller::donationProjectInsert);
```

---

### TalentDonationProjectController.java

```java
 * PROJECT  : 재능기부 프로젝트
 * NAME  :  TalentDonationProjectController.java
 
// 기존 코드
public void donationProjectInsert(TalentDonationProject project){
	
		String projectName = project.getTalentDonationProjectName();
		if(projectName != null && projectName.length() != 0) {
			try {
				
				service.donationProjectInsert(project);
				EndView.successMessage("새로운 프로젝트 등록 성공했습니다.");
				
			} catch (Exception e) {
				FailView.failViewMessage(e.getMessage()); //실패인 경우 예외로 end user 서비스
				e.printStackTrace();
			}
		}else {
			FailView.failViewMessage("입력 부족, 재 확인 하세요~~");
		}
	}
	
// stream api 수정
public void donationProjectInsert(TalentDonationProject project) {
        Optional.ofNullable(project.getTalentDonationProjectName())
                .filter(name -> !name.isEmpty())
                .ifPresentOrElse(
                        name -> {
                            try {
                                service.donationProjectInsert(project);
                                EndView.successMessage("새로운 프로젝트 등록 성공했습니다.");
                            } catch (Exception e) {
                                FailView.failViewMessage(e.getMessage());
                                e.printStackTrace();
                            }
                        },
                        () -> FailView.failViewMessage("입력 부족, 재 확인 하세요~~")
                );
    }
```

---

### TalentDonationProjectService.java

#### project 검색

```java
 * PROJECT  : 재능기부 프로젝트
 * NAME  :  TalentDonationProjectService.java
 

* 모든 Project 검색
// 기존 코드
public ArrayList<TalentDonationProject> getDonationProjectsList() {
		return donationProjectList;
	}

	public TalentDonationProject getDonationProject(String projectName) {
		for (TalentDonationProject project : donationProjectList) {
			if (project != null && project.getTalentDonationProjectName().equals(projectName)) {
				return project; //메소드 자체의 종료
			}
		}

		return null;
	}

// stream api 수정
	public ArrayList<TalentDonationProject> getDonationProjectsList() {
		return donationProjectList;
	}

	public TalentDonationProject getDonationProject(String projectName) {
		return donationProjectList.stream()
				.filter(project -> project != null && project.getTalentDonationProjectName().equals(projectName))
				.findFirst().orElse(null);
	}
```

#### 프로젝트 추가

```java
* PROJECT  : 재능기부 프로젝트
* NAME  :  TalentDonationProjectService.java

* 프로젝트 추가
// 기존 코드
public void donationProjectInsert(TalentDonationProject project) throws Exception {
		TalentDonationProject p = getDonationProject(project.getTalentDonationProjectName());
		if (p != null) {
			throw new Exception("해당 project명은 이미 존재합니다. 재 확인하세요");
		}
		donationProjectList.add(project);	
	}
	
// stream api 수정
	public void donationProjectInsert(TalentDonationProject project) throws Exception {
		if (donationProjectList.stream()
				.anyMatch(p -> p.getTalentDonationProjectName().equals(project.getTalentDonationProjectName()))) {
			throw new Exception("해당 project명은 이미 존재합니다. 재 확인하세요");
		}

		donationProjectList.add(project);
	}
```

#### 기부자 수정

```java
* PROJECT  : 재능기부 프로젝트
* NAME  :  TalentDonationProjectService.java

* 기부자 수정
// 기존 코드
public void donationProjectUpdate(String projectName, Donator people) throws Exception {
		for (TalentDonationProject project : donationProjectList) {
			if (project != null && project.getTalentDonationProjectName().equals(projectName)) {
				if (people != null) {
					project.setProjectDonator(people);
					break;
				} else {
					throw new Exception("프로젝트 이름은 있으나 기부자 정보 누락 재확인 하세요");
				}
			} else {
				throw new Exception("프로젝트 이름과 기부자 정보 재 확인 하세요");
			}
		}
	}
	
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
```

#### 수혜자 수정

```java
* PROJECT  : 재능기부 프로젝트
* NAME  :  TalentDonationProjectService.java

* 수혜자 수정

// 기존 코드
public void beneficiaryProjectUpdate(String projectName, Beneficiary people) {
		for (TalentDonationProject project : donationProjectList) {
		
			if (project != null && project.getTalentDonationProjectName().equals(projectName)) {
				project.setProjectBeneficiary(people);
				break;
			}
		}
	}

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
```

#### project 삭제

```java
* PROJECT  : 재능기부 프로젝트
* NAME  :  TalentDonationProjectService.java

* project 삭제
// 기존 코드
public void donationProjectDelete(String projectName) {
		TalentDonationProject project = getDonationProject(projectName);

		if (project != null) {
			donationProjectList.remove(project);
		}
	}
	
// stream api 수정
public void donationProjectDelete(String projectName) {
		donationProjectList = donationProjectList.stream()
				.filter(project -> !project.getTalentDonationProjectName().equals(projectName))
				.collect(Collectors.toCollection(ArrayList::new));
}
```
