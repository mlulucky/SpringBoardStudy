console.log("포함됨");

// 팔로우 링크 버튼 (통신을 직접할 필요가 없다)
async function following(toId,btn){ // 팔로우버튼
    let status=await registerFollow(toId,false);
    if (status === "1") { // === 형변환하는 비교, 형변환 안하는 비교. "1" 문자열을 숫자 1로 같음으로 할 경우, == 형변환을 하면 시간이 걸려서
            // == 은 값만 같으면 되는 비교(형변환(문자열->정수) 시간이 걸린다) // 문자열이니까 문자열 타입 같은지까지 비교하는게 더 낫다? // status "1" => status==1 로 비교하면 형변환이 되서 시간이 걸린다.
            alert("팔로우 성공")
            btn.closest(".followCont").remove();
    } else { // register=0 일때
        alert("팔로우 실패");
    }
}

// 팔로우등록 (팔로워/팔로잉) / follower - true(팔로워),false(팔로잉)
async function registerFollow(toId,follower){ // 팔로우버튼
    let url=`/follow/${toId}/${follower}/handler.do`
    const resp=await fetch(url,{method:"POST"});
    if(resp.status===200) {
        let status = await resp.text(); // 문자열 "0" 또는 "1"
        return status;
    }else if(resp.status===400){
        alert("로그인 하셔야 이용할 수 있는 서비스 입니다.");
    }else if(resp.status===500) { // 팔로잉 했는데 한번더 팔로잉하면 db 오류 발생 or db 통신오류 // 500 에러
        alert("이미 팔로잉되었거나 또는 오류가 발생했으니 새로고침하고 다시 시도하세요");
    }
}

async function removeFollow(fromId,follower){ // btn : 삭제버튼
    // follower true(팔로워 삭제), false(팔로잉 삭제)
    let url=`/follow/${fromId}/${follower}/handler.do`
    let method="DELETE";
    const resp=await fetch(url,{method:method});
    if(resp.status===200){
        return await resp.text(); // 0 실패, 1 성공
    } else if(resp.status===400){
        alert("로그인 하셔야 이용할 수 있는 서비스 입니다.");
    }else if(resp.status===500) { // db 통신오류 // 500 에러
        alert("오류가 발생했으니 새로고침하고 다시 시도하세요");
    }
}


async function toggleFollower(fromId,btn){
    let active=btn.classList.contains("active");
    if(active){ // active 가 true 이면 이미 삭제됨(팔로워를 다시 등록) // 취소를 번복하는 건 없고. 다시 등록하기
        // btn.classList.remove("active");
        // alert("팔로워 삭제 취소(팔로워 다시 등록)");
        let register=await registerFollow(fromId,true);
        if(register==="1"){
            btn.classList.remove("active");
            alert("팔로워 삭제 취소(팔로워를 다시 등록)"); // 팔로워 삭제버튼을 2번 누르면 => 팔로워 다시 등록
        }else{
            alert("팔로워 삭제 취소 실패"); // 🔥언제?
        }

    }else { // 아직 삭제되지 않음(팔로워를 삭제)
        let remove=await removeFollow(fromId,true);
        if(remove==="1"){
            btn.classList.add("active");
            alert("팔로워 삭제 성공");
        }else {
            alert("팔로워 삭제 실패");
        }
    }
}


// 팔로잉 리스트
// 누구를 취소할건지를 파라미터로 받아야 한다. 내가 팔로잉하는 사람 toId
async function toggleFollowing(toId, btn){ // 팔로잉버튼 - 한번누르면 팔로우, 두번누르면 팔로우 취소
    // console.log(toId,btn);
    let active=btn.classList.contains("active");
    if(active) { // active 클래스가 있으면~ 팔로잉 취소
        let remove=await removeFollow(toId,false); // follow 가 false // false 가 이사람을 to_id 로 쓰겠다 ?
        if(remove>0){
            alert("팔로잉을 취소 성공");
            btn.classList.remove("active");
        }else { // 에러처리
            alert("팔로잉 취소 실패")
        }
    }else { // 다시 팔로잉
        let register=await registerFollow(toId,false);
        if(register>0){ // 자바스크립트는 문자열을 비교연산하면 => 수로 형변환 한다.
            alert("팔로잉 성공");
            btn.classList.add("active");
        }else {
            alert("팔로잉 실패");
        }
    }
}






// async function following(toId,btn){ // 팔로우버튼
//     let url=`/follow/${toId}/handler.do`
//     // alert(url);
//     const resp=await fetch(url,{method:"POST"});
//     if(resp.status===200) {
//         let status = await resp.text(); // 문자열 "0" 또는 "1"
//         if (status === "1") { // === 형변환하는 비교, 형변환 안하는 비교. "1" 문자열을 숫자 1로 같음으로 할 경우, == 형변환을 하면 시간이 걸려서
//             // == 은 값만 같으면 되는 비교(형변환(문자열->정수) 시간이 걸린다) // 문자열이니까 문자열 타입 같은지까지 비교하는게 더 낫다? // status "1" => status==1 로 비교하면 형변환이 되서 시간이 걸린다.
//             alert("팔로우 성공")
//             btn.closest(".followCont").remove();
//         } else { // register=0 일때
//             alert("팔로우 실패");
//         }
//     }else if(resp.status===400){
//         alert("로그인 하셔야 이용할 수 있는 서비스 입니다.");
//     }else if(resp.status===500) { // 팔로잉 했는데 한번더 팔로잉하면 db 오류 발생 or db 통신오류 // 500 에러
//         alert("이미 팔로잉되었거나 또는 오류가 발생했으니 새로고침하고 다시 시도하세요");
//     }
// }

