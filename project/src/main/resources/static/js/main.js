// Function to increment number of dogs
function increment(id) {
    const input = document.getElementById(id);
    input.value = parseInt(input.value) + 1;
}

// Function to decrement number of dogs
function decrement(id) {
    const input = document.getElementById(id);
    if (parseInt(input.value) > 0) {
        input.value = parseInt(input.value) - 1;
    }
}

// Function to toggle login/logout state
function toggleAuth() {
    const authButton = document.getElementById('auth-button');
    const isLoggedIn = authButton.textContent === 'LOGOUT';

    if (isLoggedIn) {
        // Perform logout action here (e.g., clearing session, etc.)
        authButton.textContent = 'LOGIN';
        authButton.href = 'login.html'; // Redirect to login page
    } else {
        // Perform login action here (e.g., checking credentials, etc.)
        authButton.textContent = 'LOGOUT';
        authButton.href = '#'; // Placeholder; can be changed to an actual logout URL
    }
}

// Set the initial state of the auth button based on login status
function initializeAuthButton() {
    const authButton = document.getElementById('auth-button');
    // Check login status here (e.g., from cookies, local storage, etc.)
    const isLoggedIn = false; // Replace with actual check

    if (isLoggedIn) {
        authButton.textContent = 'LOGOUT';
        authButton.href = '#'; // Placeholder; can be changed to an actual logout URL
    } else {
        authButton.textContent = 'LOGIN';
        authButton.href = 'login.html'; // Redirect to login page
    }
}

// Initialize auth button on page load
document.addEventListener('DOMContentLoaded', initializeAuthButton);

// 스크롤 이벤트 처리
		window.addEventListener("scroll", function() {
		  const navbar = document.querySelector(".navbar");
		  const logo = document.querySelector("#logoImage");
		  
		  if (window.scrollY > 80) { // 스크롤 위치가 150px 이상이면
		    navbar.classList.add("scroll"); // 네비바의 클래스에 'scroll' 추가
			navbar.style.backgroundColor = "white"; // 배경색을 흰색으로 변경
			logo.src="/images/hotel_logo.png";
		  } else {
		    navbar.classList.remove("scroll"); // 스크롤이 위로 올라가면 클래스 제거
			navbar.style.backgroundColor="transparent";
			logo.src="/images/logo_white.png";
		  }
		});

	// 스크롤 이벤트 처리
			window.addEventListener("scroll", function() {
			  const navbar = document.querySelector(".navbar");
			  const logo = document.querySelector("#logoImage");
			  
			  if (window.scrollY > 80) { // 스크롤 위치가 150px 이상이면
			    navbar.classList.add("scroll"); // 네비바의 클래스에 'scroll' 추가
				navbar.style.backgroundColor = "white"; // 배경색을 흰색으로 변경
				logo.src="/images/hotel_logo.png";
			  } else {
			    navbar.classList.remove("scroll"); // 스크롤이 위로 올라가면 클래스 제거
				navbar.style.backgroundColor="transparent";
				logo.src="/images/logo_white.png";
			  }
			});
			



			// 강아지 정보 삭제 기능
			function removeDogInfo(element) {
			    var container = document.getElementById('dog-info-container');
			    container.removeChild(element.parentElement);
			}

			// 검색창 드롭다운 위치 설정
			function setSearchDropdownPosition() {
			    const searchInput = document.getElementById("searchInput");
			    const searchDropdown = document.getElementById("searchDropdown");

			    const rect = searchInput.getBoundingClientRect();
			    const offsetTop = rect.bottom + window.scrollY;
			    const offsetLeft = rect.left + window.scrollX;

			    searchDropdown.style.top = `40px`;
			    searchDropdown.style.left = `${offsetLeft}px`;
			    searchDropdown.style.width = `430px`;
			}

			// 검색창 드롭다운 토글
			function toggleSearchForm() {
			    const searchDropdown = document.getElementById("searchDropdown");

			    setSearchDropdownPosition();

			    if (searchDropdown.style.display === "flex") {
			        searchDropdown.style.display = "none";
			    } else {
			        searchDropdown.style.display = "flex";
			    }
			}

			// 강아지 드롭다운 토글
			function toggleDogDropdown() {
			    const dogDropdown = document.getElementById("dogDropdown");
			    dogDropdown.style.display = dogDropdown.style.display === "block" ? "none" : "block";
			}

			// 메뉴 드롭다운 토글
			function toggleMenuDropdown() {
			    const menuDropdownContent = document.getElementById("menuDropdownContent");
			    menuDropdownContent.style.display = menuDropdownContent.style.display === "block" ? "none" : "block";
			}

			// 드롭다운 외부 클릭 시 닫기
			document.addEventListener('click', function(event) {
			    const searchDropdown = document.getElementById("searchDropdown");
			    const searchInput = document.getElementById("searchInput");
			    const menuDropdown = document.getElementById("menuDropdown");
			    const menuDropdownContent = document.getElementById("menuDropdownContent");
			    const dogDropdown = document.getElementById("dogDropdown");
			    const dogButton = document.querySelector('.btn-outline-secondary'); // 강아지 버튼

			    if (!searchInput.contains(event.target) && !searchDropdown.contains(event.target)) {
			        searchDropdown.style.display = "none";
			    }

			    if (!menuDropdown.contains(event.target) && !menuDropdownContent.contains(event.target)) {
			        menuDropdownContent.style.display = "none";
			    }

			    // 강아지 드롭다운 닫기
			    if (dogDropdown && dogButton) {
			        if (!dogButton.contains(event.target) && !dogDropdown.contains(event.target)) {
			            dogDropdown.style.display = "none";
			        }
			    }
			});
			// 토글바 보이기/숨기기
					function toggleSearchForm() {
						const toggleBar = document.getElementById('toggleBar');
						toggleBar.classList.toggle('show-toggle-bar');
					}
			document.addEventListener('DOMContentLoaded', function () {
			  var offcanvasElementList = [].slice.call(document.querySelectorAll('.offcanvas'))
			  var offcanvasList = offcanvasElementList.map(function (offcanvasEl) {
			    return new bootstrap.Offcanvas(offcanvasEl)
			  })
			});


			$(function() {
			    // 기존 데스크탑용과 동일한 설정을 모바일용에도 적용
			    $('#dateRangePicker, #dateRangePickerMobile').daterangepicker({
			        locale: {
			            format: 'YYYY-MM-DD',
			            applyLabel: '적용',
			            cancelLabel: '초기화',
			        },
			        minDate: moment().startOf('day'),
			        autoApply: false,
			        showDropdowns: true,
			        opens: 'center'
			    });
			});

			// 스크롤 시 드롭다운 메뉴 위치 업데이트
			document.addEventListener('scroll', function() {
			    const searchDropdown = document.getElementById("searchDropdown");

			    if (searchDropdown.style.display === "flex") {
			        setSearchDropdownPosition();
			    }
			});

			// 숫자 증가 및 감소 기능
			function increment(id) {
			    const input = document.getElementById(id);
			    let currentValue = parseInt(input.value);
			    if (!isNaN(currentValue) && currentValue < 10) {
			        input.value = currentValue + 1;
			    }
			}

			function decrement(id) {
			    const input = document.getElementById(id);
			    let currentValue = parseInt(input.value);
			    if (!isNaN(currentValue) && currentValue > 0) {
			        input.value = currentValue - 1;
			    }
			}


			// 강아지 선택 적용 기능
			function applyDogSelection() {
			    const dogWeight1 = parseInt(document.getElementById('dogWeight1').value); // S 마리수
			    const dogWeight2 = parseInt(document.getElementById('dogWeight2').value); // M 마리수
			    const dogWeight3 = parseInt(document.getElementById('dogWeight3').value); // L 마리수

			    // 강아지 총 마리수 계산
			    const totalDogs = dogWeight1 + dogWeight2 + dogWeight3;

			    // 무게별 마리 수 텍스트
			    let dogInfoText = '';
			    if (dogWeight1 > 0) dogInfoText += `S ${dogWeight1}마리 `;
			    if (dogWeight2 > 0) dogInfoText += `M ${dogWeight2}마리 `;
			    if (dogWeight3 > 0) dogInfoText += `L ${dogWeight3}마리`;

			    // 강아지 버튼 텍스트 업데이트
			    const dogButton = document.querySelector('.btn-outline-secondary');
			    if (totalDogs > 0) {
			        dogButton.textContent = `강아지 (${dogInfoText.trim()})`;
			    } else {
			        dogButton.textContent = '강아지';
			    }

			    // 드롭다운 닫기
			    toggleDogDropdown();
				}
				// 강아지 선택 초기화 기능
				function resetDogSelection() {
				    // 모든 강아지 마리 수를 0으로 설정
				    document.getElementById('dogWeight1').value = 0;
				    document.getElementById('dogWeight2').value = 0;
				    document.getElementById('dogWeight3').value = 0;

				    // 강아지 버튼 텍스트를 초기 상태로 설정
				    const dogButton = document.querySelector('.btn-outline-secondary');
				    dogButton.textContent = '강아지';

				    // 드롭다운 닫기
				    toggleDogDropdown();
				}
			$(function() {
			    $('#dateRangePicker').daterangepicker({
			        locale: {
			            format: 'YYYY-MM-DD',
			            applyLabel: '적용',
			            cancelLabel: '초기화',
			            fromLabel: '시작일',
			            toLabel: '종료일',
			            customRangeLabel: '사용자 지정',
			            weekLabel: 'W',
			            daysOfWeek: ['일', '월', '화', '수', '목', '금', '토'],
			            monthNames: ['1월', '2월', '3월', '4월', '5월', '6월', '7월', '8월', '9월', '10월', '11월', '12월'],
			            firstDay: 1
			        },
			        minDate: moment().startOf('day'), // 오늘 날짜부터 선택 가능
			        autoApply: false, // 적용 버튼이 필요하므로 자동 적용 해제
			        showDropdowns: true, // 년도, 월을 선택할 수 있는 드롭다운 표시
			        opens: 'center',
			        autoUpdateInput: false, // 사용자가 선택할 때까지 입력 필드 업데이트 안함
			        alwaysShowCalendars: true, // 항상 달력을 표시
			        ranges: {
			            '오늘': [moment(), moment().add(1,'days')] // 오늘 버튼 추가
			        }
			    }, function(start, end, label) {
			        var nights = end.diff(start, 'days');
			        $('#dateRangePicker').val(start.format('MM/DD') + ' - ' + end.format('MM/DD') + ' (' + nights + '박)');
			    });

			    // 오늘 버튼 클릭 시 날짜는 업데이트되지만 달력은 닫히지 않음
			    $('.ranges ul li').on('click', function(event) {
			        var label = $(this).text();
			        if (label === '오늘') {
			            var startDate = moment();
			            var endDate = moment().add(1, 'days'); // 종료일을 시작일 + 1일로 설정
			            $('#dateRangePicker').data('daterangepicker').setStartDate(startDate);
			            $('#dateRangePicker').data('daterangepicker').setEndDate(endDate);
			            $('#dateRangePicker').val(startDate.format('MM/DD') + ' - ' + endDate.format('MM/DD') + ' (1박)');
			            
			            // 달력을 수동으로 다시 보여줘서 닫히지 않게 함
			            $('#dateRangePicker').data('daterangepicker').show();
			        }
			    });

			    // 초기화 (Cancel) 버튼 클릭 시 input 필드 비우기, 달력 유지
			    $('#dateRangePicker').on('cancel.daterangepicker', function(ev, picker) {
			        $(this).val('');
			        picker.show(); // 초기화 버튼 후에도 달력 유지
			    });

			    // 적용 버튼 클릭 시 달력 닫기
			    $('#dateRangePicker').on('apply.daterangepicker', function(ev, picker) {
			        var nights = picker.endDate.diff(picker.startDate, 'days');
			        $(this).val(picker.startDate.format('MM/DD') + ' - ' + picker.endDate.format('MM/DD') + ' (' + nights + '박)');
			    });
			});
