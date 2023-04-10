// th:inline : 자바스크립트를 써서 타임리프 속성을 쓸수도 있다는 뜻
async function boardLikeHandler(status,bId){
    const likeCont=document.getElementById("likeCont"+bId);
    console.log(likeCont);
    // 좋아요버튼을 눌렀을때, 버튼의 부모를 찾는 것
    // console.log(this); // window 나온다 => 설명은 loginLikes 에

    // 좋아요 상태 어떤 보드를 바꿀건지
    let url=`/board/like/${status}/${bId}/handler.do`
    // alert(url);
    const resp=await fetch(url);
    if(resp.status===200){
        const json=await  resp.json();
        if(json.handler>0){
            let html=await readLike(bId);
            if(html){
                likeCont.innerHTML=html;
                alert(json.status+" "+json.handlerType+" 성공");
            }else {
                alert(json.status+" "+json.handlerType+" 성공(불러오기 실패 새로고침)");
            }
        }else {
            alert(json.status+" "+json.handlerType+" 실패");
        }
    }else if(resp.status===400){
        alert("로그인 하셔야 이용 가능한 서비스 입니다.(잘못된 요청)");
    }else { // 실패
        alert("실패 status:"+resp.status);
    }
    // likes 에 bId 가 없어서 받아와야 한다. => 대괄호 두개 => 타임리프를 불러오는것!
}

async function readLike(bId){
    let url=`/board/like/${bId}/read.do`;
    const resp=await fetch(url);
    if(resp.status===200){
        const html=await resp.text();
        console.log(html);
        return html;
    }
}
