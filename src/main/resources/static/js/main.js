angular.module("petShopModule", ['ui.router'])
    .run(function ($rootScope) {
        $rootScope.loginedAccount = {
            phoneNumber: "",
            accountLevel: -1
        };
    })
    .config(function ($stateProvider) {
        var marketView = {
            name: 'marketView', url: '/marketView', templateUrl: 'market.html'
        };
        var loginView = {
            name: 'loginView', url: '/loginView', templateUrl: 'login.html'
        };
        var registerView = {
            name: 'registerView', url: '/registerView', templateUrl: 'register.html'
        };
        var waitingAccountView = {
            name: 'waitingAccountView', url: '/waitingAccountView', templateUrl: 'waitingAccount.html'
        };
        var merchandiseView = {
            name: 'merchandiseView', url: '/merchandiseView', templateUrl: 'merchandise.html'
        };
        var trolleyView = {
            name: 'trolleyView', url: '/trolleyView', templateUrl: 'trolley.html'
        };
        var indentView = {
            name: 'indentView', url: '/indentView', templateUrl: 'indent.html'
        };
        var copeIndentView = {
            name: 'copeIndentView', url: '/copeIndentView', templateUrl: 'copeIndent.html'
        };
        var pointView = {
            name: 'pointView', url: '/pointView', templateUrl: 'point.html'
        };
        var myPetsView = {
            name: 'myPetsView', url: '/myPetsView', templateUrl: 'myPets.html'
        };
        var pointMerchandiseView = {
            name: 'pointMerchandiseView', url: '/pointMerchandiseView', templateUrl: 'pointMerchandise.html'
        };
        $stateProvider.state(loginView);
        $stateProvider.state(registerView);
        $stateProvider.state(marketView);
        $stateProvider.state(waitingAccountView);
        $stateProvider.state(merchandiseView);
        $stateProvider.state(trolleyView);
        $stateProvider.state(indentView);
        $stateProvider.state(copeIndentView);
        $stateProvider.state(pointView);
        $stateProvider.state(myPetsView);
        $stateProvider.state(pointMerchandiseView);
    })
    .controller("indexController", function ($scope, $rootScope) {
        $scope.signOutClick = function () {
            var confirmSignOut = confirm("Will you sign out?");
            if (confirmSignOut === true) {
                $rootScope.loginedAccount.accountLevel = -1;
                $rootScope.loginedAccount.phoneNumber = "";
                top.location = "/index.html#!/marketView";
            }
        }
    })
    .controller("loginController", function ($scope, $http, $rootScope) {
        $scope.loginMessage = "";
        $scope.loginClick = function (loginAccount) {
            $http.post("/account/login", loginAccount).then(function (value) {
                $scope.loginMessage = value.data;
                if ($scope.loginMessage === "no this account"
                    || $scope.loginMessage === "wrong password"
                    || $scope.loginMessage === "wait workers to check") {
                    alert($scope.loginMessage);
                } else {
                    $rootScope.loginedAccount.phoneNumber = loginAccount.phoneNumber;
                    $rootScope.loginedAccount.accountLevel = parseInt(value.data);
                    if ($rootScope.loginedAccount.accountLevel === 1) {
                        alert("welcome!consumer!")
                    } else if ($rootScope.loginedAccount.accountLevel === 2) {
                        alert("welcome!seller!");
                    } else if ($rootScope.loginedAccount.accountLevel === 0) {
                        alert("welcome!worker!");
                    } else {
                        alert($rootScope.loginedAccount.accountLevel);
                    }
                    top.location = "/index.html#!/marketView";
                }
            }, function (reason) {
                console.log(reason.data);
            });
        }

    })
    .controller("registerController", function ($scope, $http) {
        $scope.registerMessage = "";
        $scope.registerClick = function (registerAccount) {
            $http.post("/account/register", registerAccount).then(function (value) {
                $scope.registerMessage = value.data;
                alert($scope.registerMessage);
                top.location = "/index.html#!/loginView";
            }, function (reason) {
                console.log(reason.data);
            });
        }
    })
    .controller("marketController", function ($scope, $http, $rootScope) {
        $scope.merchandiseList = [{
            merchandiseId: 0, accountId: 0, sellerNickName: "", merchandiseName: "", tag: 0, price: 0
        }];
        $scope.getMerchandiseListFun = function () {
            $http.get("/getMerchandiseList/-1").then(function (value) {
                $scope.merchandiseList = value.data;
            }, function (reason) {
                console.log(reason.data);
            });
        };
        $scope.addMerchandiseToTrolley = function (merchandise, number) {
            $scope.addingTrolley = {
                merchandiseId: merchandise.merchandiseId,
                phoneNumber: $rootScope.loginedAccount.phoneNumber,
                number: number
            };
            $http.post("/addMerchandiseToTrolley", $scope.addingTrolley).then(function (value) {
                alert(value.data);
            }, function (reason) {
                console.log(reason.data);
            });
        };
        $scope.getMerchandiseListFun();
    })
    .controller("trolleyController", function ($scope, $http, $rootScope) {
        $scope.myTrolleyList = [{
            trolleyId: 0, merchandiseName: "", merchandiseId: 0, number: 0, tag: 0, sellerNickName: "", price: 0
        }];
        $scope.getMyTrolleyListFun = function () {
            $http.get("/getMyTrolleyList/" + $rootScope.loginedAccount.phoneNumber).then(function (value) {
                $scope.myTrolleyList = value.data;
            }, function (reason) {
                console.log(reason.data);
            });
        };
        $scope.orderIndentClick = function () {
            $http.post("/orderIndent", $rootScope.loginedAccount.phoneNumber).then(function (value) {
                alert(value.data);
                $scope.getMyTrolleyListFun();
            }, function (reason) {
                console.log(reason.data);
            });

        };
        $scope.getMyTrolleyListFun();
    })
    .controller("indentController", function ($scope, $http, $rootScope) {
        $scope.myIndentList = [{
            indentId: 0, merchandiseName: "", sellerNickName: "", tag: 0, price: 0, number: 0, date: "", status: 0
        }];
        $scope.getMyIndentListFun=function () {
            $http.get("/getMyIndentList/" + $rootScope.loginedAccount.phoneNumber).then(function (value) {
                $scope.myIndentList = value.data;
            }, function (reason) {
                console.log(reason.data);
            });
        }
        $scope.cancelIndentFun = function (indentId) {
            $http.post("/cancelIndent", indentId).then(function (value) {
                alert(value.data);
                $scope.getMyIndentListFun();
            }, function (reason) {
                console.log(reason.data);
            });
        };
        $scope.finishIndentFun = function (indentId) {
            $http.post("/finishIndent", indentId).then(function (value) {
                alert(value.data);
                $scope.getMyIndentListFun();
            }, function (reason) {
                console.log(reason.data);
            });
        };
        $scope.getMyIndentListFun();
    })
    .controller("pointController",function ($scope, $http, $rootScope) {
        $scope.myPoint = 0;
        $scope.pointMerchandiseList = [{
            merchandiseId: 0, accountId: 0, sellerNickName: "", merchandiseName: "", tag: 0, price: 0
        }];
        $scope.getMyPointFun=function () {
            $http.get("/account/getMyPoint/" + $rootScope.loginedAccount.phoneNumber).then(function (value) {
                $scope.myPoint = value.data;
            }, function (reason) {
                console.log(reason.data);
            });
        }
        $scope.getPointMerchandiseListFun = function () {
            $http.get("/getPointMerchandiseList").then(function (value) {
                $scope.pointMerchandiseList = value.data;
            }, function (reason) {
                console.log(reason.data);
            });
        };
        $scope.exchangePointMerchandiseFun = function (pointMerchandise,number) {
            var exchangePointMerchandiseConfirm = confirm("是否兑换\"" + pointMerchandise.merchandiseName + "\"");
            if (exchangePointMerchandiseConfirm === true) {
                $scope.exchangingPointMerchandise = {
                    phoneNumber: $rootScope.loginedAccount.phoneNumber,
                    merchandiseId: pointMerchandise.merchandiseId,
                    number:number
                };
                $http.post("/exchangePointMerchandise", $scope.exchangingPointMerchandise).then(function (value) {
                    alert(value.data);
                    $scope.getMyPointFun();
                    $scope.getPointMerchandiseListFun();
                }, function (reason) {
                    console.log(reason.data);
                });
            }
        };
        $scope.getMyPointFun();
        $scope.getPointMerchandiseListFun();
    })
    .controller("myPetsController",function ($scope, $http, $rootScope) {
        $scope.myPetList = [{
            petId: 0, sellerNickName: "", petName: ''
        }];
        $scope.getMyPetsFun = function () {
            $http.get("/account/getMyPetList/"+$rootScope.loginedAccount.phoneNumber).then(function (value) {
                $scope.myPetList = value.data;
            }, function (reason) {
                console.log(reason.data);
            });
        };
        $scope.getMyPetsFun();
    })
    .controller("merchandiseController", function ($scope, $http, $rootScope) {
        $scope.myMerchandiseList = [{
            merchandiseId: 0, merchandiseName: "", tag: 0, price: 0
        }]
        $scope.getMyMerchandiseListFun = function () {
            $http.get("getMerchandiseList/" + $rootScope.loginedAccount.phoneNumber).then(function (value) {
                $scope.merchandiseList = value.data;
            }, function (reason) {
                console.log(reason.data);
            });
        };
        $scope.addNewMerchandiseClick = function (newMerchandise) {
            newMerchandise.phoneNumber = $rootScope.loginedAccount.phoneNumber;
            $http.post("seller/addNewMerchandise", newMerchandise).then(function (value) {
                alert(value.data);
                $scope.getMyMerchandiseListFun();
            }, function (reason) {
                console.log(reason.data);
            });
        };
        $scope.deleteMerchandiseFun = function (merchandiseId) {
            var confirmDeleteMerchandise = confirm("删除此货物?");
            if (confirmDeleteMerchandise === true) {
                $http.post("/seller/deleteMerchandise", merchandiseId).then(function (value) {
                    alert(value.data);
                    $scope.getMyMerchandiseListFun();
                }, function (reason) {
                    console.log(reason.data);
                });

            }
        };
        $scope.getMyMerchandiseListFun();

    })
    .controller("copeIndentController",function ($scope, $http, $rootScope) {
        $scope.receivingIndentList = [{
            indentId: 0,accountId:0, merchandiseId: 0, merchandiseName: "",tag:0, price: 0, number: 0, date: ""
    }];
        $scope.getReceivingIndentListFun=function () {
            $http.get('/getReceivingIndentList/'+$rootScope.loginedAccount.phoneNumber).then(function (value) {
                $scope.receivingIndentList = value.data;
            }, function (reason) {
                console.log(reason.data);
            });
        }
        $scope.receiveIndentFun = function (receivingIndentId) {
            $http.post("/receiveIndent", receivingIndentId).then(function (value) {
                alert(value.data);
                $scope.getReceivingIndentListFun();
            }, function (reason) {
                console.log(reason.data);
            });
        };
        $scope.getReceivingIndentListFun();
    })
    .controller("waitingAccountController", function ($scope, $http) {
        $scope.waitingAccountList = [{
            accountId: 0, phoneNumber: "", nickName: ""
        }]
        $scope.getWaitingAccountFun = function () {
            $http.get("/account/getWaitingAccount").then(function (value) {
                $scope.waitingAccountList = value.data;
            }, function (reason) {
                console.log(reason.data);
            });
        };
        $scope.permitClick = function (waitingAccountId) {
            $http.post("/account/permitSeller", waitingAccountId).then(function (value) {
                alert(value.data);
                $scope.getWaitingAccountFun();
            }, function (reason) {
                console.log(reason.data);
            });
        };
        $scope.addNewAccountByWorkerFun = function (newAccount) {
            $http.post("/account/addNewAccountByWorker", newAccount).then(function (value) {
                alert(value.data);
            }, function (reason) {
                console.log(reason.data);
            });
        };
        $scope.getWaitingAccountFun();
    })
    .controller("pointMerchandiseController",function ($scope, $http, $rootScope) {
        $scope.pointMerchandiseList = [{
            merchandiseId: 0, accountId: 0, sellerNickName: "", merchandiseName: "", tag: 0, price: 0
        }];
        $scope.getPointMerchandiseListFun = function () {
            $http.get("/getPointMerchandiseList").then(function (value) {
                $scope.pointMerchandiseList = value.data;
            }, function (reason) {
                console.log(reason.data);
            });
        };
        $scope.deletePointMerchandiseFun = function (merchandiseId) {
            var deletePointMerchandiseConfirm = confirm("确定删除此积分商品?");
            if (deletePointMerchandiseConfirm === true) {
                $http.post("/deletePointMerchandise", merchandiseId).then(function (value) {
                    alert(value.data);
                    $scope.getPointMerchandiseListFun();
                }, function (reason) {
                    console.log(reason.data);
                });
            }
        };
        $scope.addPointMerchandiseFun = function (merchandiseId) {
            $http.post("/addPointMerchandise", merchandiseId).then(function (value) {
                alert(value.data);
                $scope.getPointMerchandiseListFun();
            }, function (reason) {
                console.log(reason.data);
            });
        };
        $scope.getPointMerchandiseListFun();
    });