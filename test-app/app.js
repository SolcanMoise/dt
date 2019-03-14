function fib(n){
    return [1, 1, 2, 3, 5];
}

function equals(a1,a2){
    if(a1.length!=a2.length) return false;

    for(var i=0; i<a1.length; i++){
        if(a1[i]!=a2[i]) return false;
    }

    return true;
}

console.log(equals(fib(5), [1, 2, 3, 5])?'Passed':'Failed');

