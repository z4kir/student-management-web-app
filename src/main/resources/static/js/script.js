function showPassword() {
    var x = document.getElementById("password");
    var y = document.getElementById("password2");
    if (x.type == "password") {
        x.type = "text";
    } else {
        x.type = "password";
    }
    if (y.type == "password") {
        y.type = "text";
    } else {
        y.type = "password";
    }

}

const showLoginPassword = () => {
    var y = document.getElementById("password");
    if (y.type == "password") {
        y.type = "text";
    } else {
        y.type = "password";
    }
}


function homeNavLink() {
    var navlinkHome = document.getElementById('home');
    var navlinkSignIn = document.getElementById('signIn');
    var navlinkLogin = document.getElementById('logIn');

    navlinkHome.classList.add("active");
    navlinkHome.setAttribute("aria-current", "page");

    if (navlinkLogin.classList.contains("active")) {
        navlinkLogin.classList.remove("active");
        navlinkLogin.removeAttribute('aria-current');
    }
    if (navlinkSignIn.classList.contains("active")) {
        navlinkSignIn.classList.remove("active");
        navlinkSignIn.removeAttribute('aria-current');
    }

}


function toggleSwitchOff() {
    var sidebar = document.getElementById("sideba");
    var content = document.getElementById("contents");
    var overlay = document.getElementById('overlay');

    //    hide side bar

    sidebar.classList.remove("animation");
    content.style.marginLeft = "0%";
    overlay.classList.remove("overly_open");


}

function toggleSwitchOn() {
    //    show side bar
    var sidebar = document.getElementById("sideba");
    var content = document.getElementById("contents");
    var overlay = document.getElementById('overlay');

    sidebar.classList.add("animation");
    content.style.marginLeft = "20%";
    overlay.classList.add("overly_open");

}

//    sidebar active
const activeSelector = (appendActive, sidebar) => {


    switch (appendActive) {

        case 'setting':
            document.getElementById(appendActive).classList.add("activeBar");

            break;


        case 'new':
            document.getElementById(appendActive).classList.add("activeBar");

            break;

        case 'studentID':
            document.getElementById(appendActive).classList.add("activeBar");

            break;

        case 'profile':
            document.getElementById(appendActive).classList.add("activeBar");

            break;

        case 'setting':
            document.getElementById(appendActive).classList.add("activeBar");
            break;

        default :
            document.getElementById('studentList').classList.add("activeBar")
            break;

    }
}
document.addEventListener("DOMContentLoaded", function () {
    let url = window.location.href.split("/");
    let currentPageLength = url.length;
    let currentPage = url[5]
    if (currentPageLength > 4) {
        // Add active class to the current button (highlight it)
        let header = document.getElementById("sideba");

        activeSelector(currentPage, header);


    }


})


//    to prevent initial transition of side bar
window.addEventListener("load", () => {
    document.body.classList.remove("preload");

    let url = window.location.href.split("/");
    let currentPageLength = url.length;
    let currentPage = url[4];
    if (currentPageLength > 4) {
        document.getElementById('overlay').style.display = "block"
    }

})

//search bar script

//below variable counts the number of data
let datalength;

// 1-search from student account
const search = () => {
    let query = document.getElementById("search-input").value;

    if (query == "") {
        document.getElementById("search-result").style.visibility = "hidden";
        document.getElementById("search-input").classList.remove("search-field-radius");

    } else {

        const query_Arr = query.split(" ");
        const firstname = query_Arr[0];
        const lastname = query_Arr[1];


        //    sending request to server
        let url;
        if (query_Arr.length == 1) {
            url = 'http://localhost:8080/search?firstName=' + firstname;

        } else {
            url = 'http://localhost:8080/search?firstName=' + firstname + '&lastName=' + lastname;

        }

        fetch(url).then((response) => {
            return response.json();
        }).then((data) => {

            let text = '<div class="list-group">';
            data.forEach((students) => {
                text += '<a href="http://localhost:8080/std/students/search?firstName=' + students.firstName + '&lastName=' + students.lastName + '" class="list-group-item list-group-action">' + students.firstName + ' ' + students.lastName + '</a>';
            })
            text += '</div>';

            datalength = data.length;
            if (data.length != 0) {
                document.getElementById("search-result").innerHTML = text;
                document.getElementById("search-input").classList.add("search-field-radius");
                document.getElementById("search-result").style.visibility = "visible";
            }


        })
    }
}

// 2-search from Teacher account
//  a.Students list search


const teacherSearch = () => {
    let query = document.getElementById("search-input").value;

    if (query == "") {
        document.getElementById("search-result").style.visibility = "hidden";
        document.getElementById("search-input").classList.remove("search-field-radius");

    } else {

        const query_Arr = query.split(" ");
        const firstname = query_Arr[0];
        const lastname = query_Arr[1];


        //    sending request to server

        let url;
        if (query_Arr.length == 1) {
            url = 'http://localhost:8080/teacher/search?firstName=' + firstname;

        } else {
            url = 'http://localhost:8080/teacher/search?firstName=' + firstname + '&lastName=' + lastname;

        }

        fetch(url).then((response) => {
            return response.json();
        }).then((data) => {

            let text = '<div class="list-group">';
            data.forEach((students) => {
                text += '<a href="http://localhost:8080/user/students/search?firstName=' + students.firstName + '&lastName=' + students.lastName + '" class="list-group-item list-group-action">' + students.firstName + ' ' + students.lastName + '</a>';
            })
            text += '</div>';

            datalength = data.length;

            if (data.length == 0) {
                document.getElementById("search-result").style.visibility = "hidden";

            }
            if (data.length != 0) {
                document.getElementById("search-result").innerHTML = text;
                document.getElementById("search-input").classList.add("search-field-radius");
                document.getElementById("search-result").style.visibility = "visible";
            }


        })


    }


}


//   b.Students Id search
const StudentIdSearch = () => {

    let query = document.getElementById("search-input").value;

    if (query == "") {
        document.getElementById("search-result").style.visibility = "hidden";
        document.getElementById("search-input").classList.remove("search-field-radius");

    } else {

        const query_Arr = query.split(" ");
        const firstname = query_Arr[0];
        const lastname = query_Arr[1];


        //    sending request to server

        let url;
        if (query_Arr.length == 1) {
            url = 'http://localhost:8080/studentID/search?firstName=' + firstname;

        } else {
            url = 'http://localhost:8080/studentID/search?firstName=' + firstname + '&lastName=' + lastname;
        }

        fetch(url).then((response) => {
            return response.json();
        }).then((data) => {

            let text = '<div class="list-group">';
            data.forEach((users) => {
                text += '<a href="http://localhost:8080/user/students/studentID/search?firstName=' + users.a1firstName + '&lastName=' + users.a2lastName + '" class="list-group-item list-group-action">' + users.a1firstName + ' ' + users.a2lastName + '</a>';
            })
            text += '</div>';


            datalength = data.length;
            if (data.length == 0) {
                document.getElementById("search-result").style.visibility = "hidden";

            }

            if (data.length != 0) {
                document.getElementById("search-result").innerHTML = text;
                document.getElementById("search-input").classList.add("search-field-radius");
                document.getElementById("search-result").style.visibility = "visible";
            }


        })
    }
}

//    3.All Id search
const IdSearch = () => {

    let query = document.getElementById("search-input").value;

    if (query == "") {
        document.getElementById("search-result").style.visibility = "hidden";
        document.getElementById("search-input").classList.remove("search-field-radius");

    } else {

        const query_Arr = query.split(" ");
        const firstname = query_Arr[0];
        const lastname = query_Arr[1];


        //    sending request to server

        let url;
        if (query_Arr.length == 1) {
            url = 'http://localhost:8080/idList/search?firstName=' + firstname;


        } else {
            url = 'http://localhost:8080/idList/search?firstName=' + firstname + '&lastName=' + lastname;
        }

        fetch(url).then((response) => {
            return response.json();
        }).then((data) => {

            let text = '<div class="list-group">';
            data.forEach((users) => {
                text += '<a href="http://localhost:8080/admin/zakir/idList/search?firstName=' + users.a1firstName + '&lastName=' + users.a2lastName + '" class="list-group-item list-group-action">' + users.a1firstName + ' ' + users.a2lastName + '</a>';
            })
            text += '</div>';

            datalength = data.length;

            if (data.length == 0) {
                document.getElementById("search-result").style.visibility = "hidden";

            }
            if (data.length != 0) {
                document.getElementById("search-result").innerHTML = text;
                document.getElementById("search-input").classList.add("search-field-radius");
                document.getElementById("search-result").style.visibility = "visible";
            }


        })
    }
}


//  applying key listener to search result list to move up and down with the help of arrow keys
document.addEventListener("keydown", (e) => {
    let searchInput = document.getElementById("search-input");
    let searchResult = document.getElementById("search-result");
    let resultList = document.getElementsByClassName("list-group-item");


    //For moving down

    if (document.activeElement == searchInput && datalength > 0) {

        if (e.key == "ArrowDown") {

            // searchInput.blur();
            resultList.item(0).focus();
            resultList.item(0).classList.add("activeResult");
        }
    } else {

        if (e.key == "ArrowDown") {
            for (let i = 0; i < (resultList.length - 1); i++) {

                if (document.activeElement == resultList.item(i)) {

                    resultList.item(i).classList.remove("activeResult");
                    resultList.item((i + 1)).focus();
                    resultList.item((i + 1)).classList.add("activeResult");
                    break;
                }
            }
        }
    }

    // for moving up

    if (document.activeElement == resultList.item(0) && datalength > 0) {

        if (e.key == "ArrowUp") {
            // searchInput.blur();
            searchInput.focus();
        }
    } else {

        if (e.key == "ArrowUp") {
            for (let i = 1; i < (resultList.length); i++) {

                if (document.activeElement == resultList.item(i)) {
                    resultList.item(i).classList.remove("activeResult");
                    resultList.item((i - 1)).focus();
                    resultList.item((i - 1)).classList.add("activeResult");
                    break;
                }
            }
        }
    }

}, false)

//    change password and email

const showChangePassword = () => {
    document.getElementById("changePassword").classList.remove("changePassword");
    document.getElementById("pchose").classList.add("choseSelected")
    document.getElementById("echose").classList.remove("choseSelected")
    document.getElementById("changeEmail").classList.add("changeEmail")
    document.getElementById("sett").style.visibility = "visible";

}


const showChangeEmail = () => {
    document.getElementById("changePassword").classList.add("changePassword");
    document.getElementById("changeEmail").classList.remove("changeEmail")
    document.getElementById("pchose").classList.remove("choseSelected")
    document.getElementById("echose").classList.add("choseSelected")
    document.getElementById("sett").style.visibility = "visible";

}

document.addEventListener("DOMContentLoaded", function () {


    let url = window.location.href.split("/");
    let currentPageLength = url.length;
    let currentPage = url[5];


    if (currentPage == "profile" && currentPageLength == 6) {
        if (role != null) {
            let roleArr = role.split("_");
            let Role = roleArr[1].toLowerCase();
            document.getElementById("role").innerHTML = Role;
        }
    }

    if (currentPage == "setting") {
        if (failCheck == "password" || success == 'password') {
            document.getElementById("changePassword").classList.remove("changePassword");
            document.getElementById("pchose").classList.add("choseSelected")
            document.getElementById("echose").classList.remove("choseSelected")
            document.getElementById("changeEmail").classList.add("changeEmail")
            document.getElementById("sett").style.visibility = "visible";
        }
        if (failCheck == "email" || success == "email") {
            document.getElementById("changePassword").classList.add("changePassword");
            document.getElementById("changeEmail").classList.remove("changeEmail")
            document.getElementById("pchose").classList.remove("choseSelected")
            document.getElementById("echose").classList.add("choseSelected")
            document.getElementById("sett").style.visibility = "visible";
        }
    }
//removing table class for small screen
    const screenSize = window.screen.width;


    if (screenSize < 751) {
        let table = document.getElementsByClassName("table--overflow").item(0);
        table.classList.remove("table--overflow");
    }


});

//Remove Image
const image = () => {

    if (document.getElementById("removeImage").checked == true) {
        document.getElementById("img").disabled = true;
    } else if (document.getElementById("removeImage").checked == false) {
        document.getElementById("img").disabled = false;
    }
}




