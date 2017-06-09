<?
    $userid = $_POST['userid'];
    $passwd = $__POST['password'];
    if($userid=='admin'& $passwd=='admin1234'){
    echo "ADMIN";
    }
    else if($userid == 'guest'&& $passwd == 'guest') {
        echo "GUEST";
    }
    else echo "FAIL";
?>