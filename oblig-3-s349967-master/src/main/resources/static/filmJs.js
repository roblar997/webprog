
/**
 * Legger til eventlistenere etter html er lastet inn.
 *  I tillegg blir skjemaet initalisert
 */
$(() => {
    if(sessionStorage.getItem("innlogget") == null){
        $("#loginOk").click(async (e) => {
            e.preventDefault();


            let loginData = GUIModule.extractLogin();
            let isValid = GUIModule.validateLogin(loginData);

            if (isValid) {
                await CommunicationModule.login(loginData);
                if (sessionStorage.getItem("innlogget") != null) {
                    window.location.reload();
                }
            }
        });
        $("#registrer").click( (e) => {
            e.preventDefault();


            let loginData = GUIModule.extractLogin();
            let isValid = GUIModule.validateLogin(loginData);

            if (isValid) {
                CommunicationModule.registrer(loginData);

            }
        });

        GUIModule.openLoginWindow();
        return;

    }
    else {
        $("#logUt").fadeIn();
        $("#logUt").click( async (e) => {
            e.preventDefault();

            await CommunicationModule.logUt();
            window.location.reload();
        })




        //initaliserer skjemaet
        StorageModule.getFilmValg();


        StorageModule.getBestillinger();
        //CommunicationModule.updateTable();


        $("#refresh").click((e) => {
            $("#endre").hide();
            $("#avbryt").hide();
            $("#kjop").show();


            let element = GUIModule.getTempElement();

            if (element != null) {
                let rowIndex = element.rowIndex - 1;
                if (rowIndex % 2 == 0) element.style.backgroundColor = "lightgray";
                else element.style.backgroundColor = "white";
            }

            //Unngå "blinking" av knapper når listen lastes inn. Dvs
            //at en ser knappene bevege seg

            StorageModule.slettBestillinger();
            GUIModule.deleteTable();
            StorageModule.getBestillinger();


        })
        $("#avbryt").click((e) => {

            $("#endre").hide();
            $("#avbryt").hide();
            $("#kjop").show();
            $("#slettButton").hide();
            $("#getMore").hide();

            let element = GUIModule.getTempElement();
            let rowIndex = element.rowIndex - 1;
            if (rowIndex % 2 == 0) element.style.backgroundColor = "lightgray";
            else element.style.backgroundColor = "white";


        });

        $("#endre").click((e) => {

            //Stoppe annen atferd mouseevent ville hatt.
            e.preventDefault();


            //Hent ut informasjon fra registrerings skjema
            let bestillingsData = GUIModule.extractBestilling();

            //Sjekk gyldigheten til informasjonen fra registrerings skjema
            let isValid = GUIModule.validate(bestillingsData);

            if (isValid) {
                $("#slettButton").hide();
                $("#getMore").hide();
                CommunicationModule.endreBestilling(GUIModule.getTempElement(), bestillingsData);
            }


        });

        //Når en kjøper noe
        $("#kjop").click((e) => {

                //Stoppe annen atferd mouseevent ville hatt.
                e.preventDefault();


                //Hent ut informasjon fra registrerings skjema
                let bestillingsData = GUIModule.extractBestilling();

                //Sjekk gyldigheten til informasjonen fra registrerings skjema
                let isValid = GUIModule.validate(bestillingsData);

                //Hvis isValid=false, så lages det bare en ny tabell hvis den ikke finnes.
                //Om isValid=true, så lages det en ny tabell hvis den ikke finnes, og
                //det lages en ny rad med informasjon fra personData.
                if (!isValid) GUIModule.addBestilling(bestillingsData, isValid);
                else {
                    $("#slettButton").hide();
                    $("#getMore").hide();
                    CommunicationModule.addBestilling(bestillingsData);
                }
            }
        );
        //Sletter alle rader i tabellen med oversikt over alle billetter.
        $("#slettButton").click(() => {
            $("#slettButton").hide();
            $("#getMore").hide();
            CommunicationModule.slettBestillinger();
        });


        $("#getMore").click(() => {
            $("#slettButton").hide();
            $("#getMore").hide();
            CommunicationModule.getMoreBestillinger(sessionStorage.getItem("viewTo"), (Number(sessionStorage.getItem("viewTo")) + 10));
        });


        $("#getMore").prop("disabled", false);



    }});

/**
 * Fungerer slik at alle bortsett fra siste som har oppdatert har en konsistent liste fra serveren.
 * Årsaken ligger ir at personer kan gjøre oppdateringer, etterfulgt av at sistemann gjør
 * sine oppdateringer uten at en har fått andres oppdateringer. Dermed tror sistemann
 * at en har nyeste versjon.
 *
 * Er mer eller umulig å fikse!!
 *
 * Er refresh button da!!!
 * @type {{getBestillinger: CommunicationModule.getBestillinger, addBestilling: CommunicationModule.addBestilling, getFilmValg: CommunicationModule.getFilmValg, slettBestilling: CommunicationModule.slettBestilling, slettBestillinger: CommunicationModule.slettBestillinger, updateTableOnce: CommunicationModule.updateTableOnce, updateTable: CommunicationModule.updateTable, getChangeTime: CommunicationModule.getChangeTime}}
 */
var CommunicationModule = (function(){

    function updateCallback1  (res) {


        if (res.changeTime != sessionStorage.getItem("changeDateTime")){

            StorageModule.slettBestillinger();

            $("#endre").hide();
            $("#avbryt").hide();
            $("#kjop").show();

            let element = GUIModule.getTempElement();

            if(element != null){
                let rowIndex = element.rowIndex-1;
                if(rowIndex % 2 == 0) element.style.backgroundColor = "lightgray";
                else                  element.style.backgroundColor = "white";
            }

            GUIModule.deleteTable();
            StorageModule.getBestillinger();
            StorageModule.setChangeTime(res.changeTime);

        }

    }

    function  addBestillingCallback1 (res,bestilling) {


        CommunicationModule.updateTableOnce();

        StorageModule.addBestilling(bestilling);
        GUIModule.addBestilling(bestilling, true);
        StorageModule.setChangeTime(res);

    }

    function getBestillingerCallback1 (res) {

        res.forEach((x) => {
            StorageModule.addBestilling(x);
            GUIModule.addBestilling(x, true);
        });


    }

    function sorterBestillingerCallback1  (res){

        StorageModule.setChangeTime(res);
        StorageModule.slettBestillinger();
        GUIModule.deleteTable();
        StorageModule.getBestillinger();

    }

    function endreBestillingCallback1 (res,rowIndex,element,bestillingPost){

        StorageModule.endreBestilling(rowIndex,bestillingPost);
        GUIModule.endreBestilling(element,bestillingPost);
        StorageModule.setChangeTime(res);



    }

    function slettBestillingCallback1 (res,rowIndex,element){

        StorageModule.removeBestilling(rowIndex);
        GUIModule.slettBestilling(element);
        StorageModule.setChangeTime(res);

    }

    function getFilmValgCallback1 (res){

        let resultArray = res.map((x) => {
            return x.filmTittel;
        });
        StorageModule.setFilmValg(resultArray);
        GUIModule.initFilmValg(resultArray);


    }

    function slettBestillingerCallback1 (res){

        $("#endre").hide();
        $("#avbryt").hide();

        $("#kjop").show();

        StorageModule.slettBestillinger();
        GUIModule.deleteTable();
        StorageModule.setChangeTime(res);



    }
    function loginCallback1 (res){
        if(res===true){
            sessionStorage.setItem("innlogget",true);

            $("#infoBoksLogin").text("Innlogging velykket");
            $("#infoBoksLogin").css("color","lightgreen");


        }
        else {
            $("#infoBoksLogin").text("Kunne ikke logge inn");
            $("#infoBoksLogin").css("color","red");
        }


    }

    function registrerCallback1 (res){
        if(res===true){
            $("#infoBoksLogin").text("Registrering velykket");
            $("#infoBoksLogin").css("color","lightgreen");
        }
        else {
            $("#infoBoksLogin").text("Kunne ikke registrere");
            $("#infoBoksLogin").css("color","red");
        }



    }
    function logUtCallback1 (res){
        sessionStorage.clear();

    }
    async function updatePTable(callback){

        await $.get("/getChangeTime", (res) => {
            callback.bind(this)(res);

        }).promise();




    }



    async function preHookSorterBestillinger(callback,next,colNr,isDesc){

        $("#spinner2").show();
        $("#getMore").hide();
        $("#placeHolderTable").hide();
        await next(callback,colNr,isDesc);

    }

    async function sorterPBestillinger(callback, colNr,isDesc){
        $("#getMore").hide();
        await $.get("/sorterBestillinger/" + colNr + "/" + isDesc,(res)=>{
            callback.bind(this)(res);
        }).promise();
    }

    async function getPMoreBestillinger(callback,from, to){

        await $.post("/getSubBestillinger/" + from + "/" + to, (res)=>{
            let nmbElem = res.length;
            StorageModule.setViewTo(Number(sessionStorage.getItem("viewTo"))+Number(nmbElem));
            callback.bind(this)(res);
        }).promise();

    }

    async function preHookEndreBestilling(callback,next,element,bestillingPost){
        let rowIndex = element.rowIndex - 1;
        let columns  =  element.getElementsByTagName("td");

        let bestillingPre = {

            filmValg: columns[0].innerText,
            antall:   columns[1].innerText,
            fornavn:  columns[2].innerText,
            etternavn: columns[3].innerText,
            telefon: columns[4].innerText,
            epost: columns[5].innerText

        };
        $("#spinner2").show();
        $("#getMore").hide();
        $("#placeHolderTable").hide();
        let bestillingsListe = JSON.stringify([bestillingPre,bestillingPost]);

        await next(callback,element,rowIndex,bestillingPost,bestillingsListe);
    }
    async function endrePBestilling(callback, element, rowIndex,bestillingPost,bestillingsListe) {



        await $.post({
            url: '/endreBestilling',
            data: bestillingsListe,
            contentType: 'application/json; charset=utf-8'
        }).done((res) => {
            callback.bind(this)(res,rowIndex,element,bestillingPost);
        }).promise();





    }

    async function preHookSlettBestilling(callback,next,element){
        //sletter elementet en prøver å endre?
        if(GUIModule.getTempElement() === element){

            $("#endre").hide();
            $("#avbryt").hide();
            $("#getMore").hide();
            $("#kjop").show();

        }

        let rowIndex = element.rowIndex - 1;
        let columns  =  element.getElementsByTagName("td");

        let bestilling = {

            filmValg: columns[0].innerText,
            antall:   columns[1].innerText,
            fornavn:  columns[2].innerText,
            etternavn: columns[3].innerText,
            telefon: columns[4].innerText,
            epost: columns[5].innerText

        }


        $("#slettButton").prop( "disabled", true );
        $("#kjop").prop( "disabled", true );

        $("#spinner2").show();
        $("#getMore").hide();
        $("#placeHolderTable").hide();

        await next(callback,element,rowIndex,bestilling);
    }
    async function slettPBestilling(callback, element,rowIndex,bestilling) {


        await $.post("/slettBestilling",bestilling, (res)=>{
            callback.bind(this)(res,rowIndex,element);
        }).promise();


    }
    async function logPUt(callback) {


        await $.post("/logout", (res)=>{
            callback.bind(this)(res);
        }).promise();


    }
    async function preHookLogUt(callback,next){

        await next(callback);
    }

    async function preHookGetFilmValg(callback,next){

        $("#spinner1").show();
        $("#filmValg").hide();

        await next(callback);
    }

    async function getPFilmValg(callback) {

        await $.get("/getFilmValg", (res)=>{
            callback.bind(this)(res);
        }).promise();


    }


    async function preHookAddBestilling(callback,next,bestilling){
        $("#spinner2").show();
        $("#placeHolderTable").hide();
        $("#getMore").hide();
        $("#slettButton").prop( "disabled", true );
        $("#kjop").prop( "disabled", true );

        await next(callback,bestilling);
    }

    async function preHookGetMoreBestillinger(callback,next,from,to){

        $("#spinner2").show();
        $("#getMore").hide();
        $("#placeHolderTable").hide();

        await next(callback,from,to);
    }

    async function addPBestilling(callback,bestilling) {



        await $.post("/lagreBestilling", bestilling, (res)=>{
            callback.bind(this)(res,bestilling);
        }).promise();


    }
    async function preHookSlettBestillinger(callback,next){
        $("#spinner2").show();
        $("#getMore").hide();
        $("#placeHolderTable").hide();

        $("#slettButton").prop( "disabled", true );
        $("#kjop").prop( "disabled", true );

        await next(callback);
    }
    async function slettPBestillinger(callback) {


        await $.post("/slettBestillinger", (res)=>{
            callback.bind(this)(res);
        }).promise();

    }

    async function preHookLogin(callback,next,loginData){
        $("#spinner3").show();

        await next(callback,loginData);
    }
    async function loginP(callback,loginData) {


        await $.post("/login",loginData, (res)=>{
            callback.bind(this)(res);
        }).promise();

    }
    async function preHookRegistrer(callback,next,loginData){
        $("#spinner3").show();
        await next(callback,loginData);
    }
    async function registrerP(callback,loginData) {


        await $.post("/registrer",loginData, (res)=>{
            callback.bind(this)(res);
        }).promise();

    }

    return {

        addBestilling:  function (bestilling){
            if(sessionStorage.getItem("innlogget") == null) return;
            let cb = addBestillingCallback1;
            let next = addPBestilling.bind(this);

            preHookAddBestilling(cb,next,bestilling).then((x)=>{
                $("#spinner2").hide();
                $("#getMore").css("display","block");
                $("#placeHolderTable").show();
                StorageModule.setViewTo(Number(sessionStorage.getItem("viewTo"))+1);
                $("#slettButton").show();
                $("#slettButton").prop( "disabled", false);
                $("#kjop").prop( "disabled", false);
                $("#infoBoks").hide();

            },(error)=>{

                //Ugyldig info sendt, så ikke vits å prøve igjen.
                if(error.status == 406 || error.status == 422){
                    $("#spinner2").hide();
                    $("#getMore").css("display","block");
                    $("#placeHolderTable").show();
                    $("#slettButton").show();
                    $("#slettButton").prop( "disabled", false);
                    $("#kjop").prop( "disabled", false);
                    $("#infoBoks").text("Data sendt til server ble ikke akseptert");
                    $("#infoBoks").show();
                    return;
                }

                $("#infoBoks").show();
                $("#infoBoks").text("Problemer med tilkobling!");

                setTimeout(() => {

                    CommunicationModule.addBestilling(bestilling) }, 10000);
            });
        },
        login:  async function (loginData){

            let cb = loginCallback1;
            let next = loginP.bind(this);

            await preHookLogin(cb,next,loginData).then((x)=>{
                $("#spinner3").hide();



            },(error)=>{

                //Ugyldig info sendt, så ikke vits å prøve igjen.
                if(error.status == 406 || error.status == 422){

                    return;
                }
                $("#spinner3").hide();
                $("#infoBoksLogin").text("Kunne ikke logge inn");
                $("#infoBoksLogin").css("color","red");


            });
        },
        registrer:  function (loginData){

            let cb = registrerCallback1;
            let next = registrerP.bind(this);

            preHookRegistrer(cb,next,loginData).then((x)=>{
                $("#spinner3").hide();


            },(error)=>{

                //Ugyldig info sendt, så ikke vits å prøve igjen.
                if(error.status == 406 || error.status == 422){

                    return;
                }
                $("#spinner3").hide();
                $("#infoBoksLogin").text("Kunne ikke registrere");
                $("#infoBoksLogin").css("color","red");


            });
        },
        logUt: async function(){
            let cb = logUtCallback1;
            let next = logPUt.bind(this);

            await preHookLogUt(cb,next).then((x)=>{

            },(error)=>{

                //Ugyldig info sendt, så ikke vits å prøve igjen.
                if(error.status == 406 || error.status == 422){

                    return;
                }



            });
        }
        ,
        getMoreBestillinger: function(from,to){
            if(sessionStorage.getItem("innlogget") == null) return;
            let cb = getBestillingerCallback1;
            let next = getPMoreBestillinger.bind(this);

            preHookGetMoreBestillinger(cb,next,from,to).then((x)=>{

                $("#spinner2").hide();
                $("#getMore").css("display","block");
                //$("#slettButton").show();
                $("#placeHolderTable").show();
                $("#infoBoks").hide();
                $("#slettButton").show();

                $("th").css("visibility","visible");
            },(error)=>{
                if(error.status == 406 || error.status == 422){
                    $("#spinner2").hide();
                    $("#getMore").css("display","block");
                    $("th").css("visibility","visible");
                    //$("#slettButton").show();
                    $("#placeHolderTable").show();
                    $("#infoBoks").hide();
                    $("#slettButton").show();
                    return;

                }
                $("#infoBoks").show();
                $("#infoBoks").text("Problemer med tilkobling!");
                setTimeout(() => {
                    CommunicationModule.getMoreBestillinger(from,to) }, 10000);
            });
        },

        slettBestillinger:  function (){
            if(sessionStorage.getItem("innlogget") == null) return;
            let cb = slettBestillingerCallback1;
            let next = slettPBestillinger.bind(this);

            preHookSlettBestillinger(cb,next).then((x)=>{
                StorageModule.setViewTo(0);
                $("#spinner2").hide();
                $("#getMore").css("display","block");
                $("#placeHolderTable").show();
                $("#slettButton").show();
                $("#slettButton").prop( "disabled", false);
                $("#kjop").prop( "disabled", false);
                $("#infoBoks").hide();

            },(error)=>{
                $("#infoBoks").show();
                $("#infoBoks").text("Problemer med tilkobling!");
                setTimeout(() => {  CommunicationModule.slettBestillinger() }, 10000);
            });
        },
        getFilmValg : function(){
            if(sessionStorage.getItem("innlogget") == null) return;
            let cb = getFilmValgCallback1;
            let next = getPFilmValg.bind(this);

            preHookGetFilmValg(cb,next).then((x)=>{
                $("#spinner1").hide();
                $("#filmValg").show();
                $("#slettButton").show();
                $("#infoBoks").hide();

            },(error)=>{
                $("#infoBoks").show();
                $("#infoBoks").text("Problemer med tilkobling!");
                setTimeout(() => {  CommunicationModule.getFilmValg(); }, 10000);

            });
        },
        updateTable: function(){
            if(sessionStorage.getItem("innlogget") == null) return;
            let cb = updateCallback1;
            updatePTable(cb).finally(()=> {  setTimeout(() => {  CommunicationModule.updateTable()}, 50000);});
        },
        updateTableOnce: function(){
            if(sessionStorage.getItem("innlogget") == null) return;
            let cb = updateCallback1;
            updatePTable(cb);
        },
        endreBestilling: function(element,postBestilling){
            if(sessionStorage.getItem("innlogget") == null) return;
            let cb = endreBestillingCallback1;
            let next = endrePBestilling.bind(this);


            preHookEndreBestilling(cb,next,element,postBestilling).then((x)=>{

                $("#spinner2").hide();
                $("#placeHolderTable").show();
                $("#infoBoks").hide();
                $("#endre").hide();
                $("#avbryt").hide();
                $("#getMore").css("display","block");
                $("#slettButton").show();
                $("#kjop").show();
                if(element != null){
                    let rowIndex = element.rowIndex-1;
                    if(rowIndex % 2 == 0) element.style.backgroundColor = "lightgray";
                    else                  element.style.backgroundColor = "white";
                }

            },(error)=>{

                //Ugyldig info sendt, så ikke vits å prøve igjen.
                if(error.status == 406 || error.status == 422){
                    $("#spinner2").hide();
                    $("#getMore").css("display","block");
                    $("#placeHolderTable").show();
                    $("#infoBoks").text("Data sendt til server ble ikke akseptert");
                    $("#infoBoks").show();
                    $("#endre").hide();
                    $("#avbryt").hide();
                    $("#slettButton").show();
                    $("#kjop").show();
                    if(element != null){
                        let rowIndex = element.rowIndex-1;
                        if(rowIndex % 2 == 0) element.style.backgroundColor = "lightgray";
                        else                  element.style.backgroundColor = "white";
                    }
                    return;

                }

                $("#infoBoks").show();
                $("#infoBoks").text("Problemer med tilkobling!");
                setTimeout(() => {  CommunicationModule.endreBestilling(element,postBestilling); }, 10000);

            });
        },
        sorterBestillinger : function(colNr,cell){
            if(sessionStorage.getItem("innlogget") == null) return;
            let cb = sorterBestillingerCallback1;
            let next =  sorterPBestillinger.bind(this);
            $("th").css("visibility","hidden");
            preHookSorterBestillinger(cb,next,colNr,cell.getAttribute("data-fargeTilstand")).then((x)=>{

                $("#spinner2").hide();
                $("#placeHolderTable").show();
                $("#infoBoks").hide();

                $("#endre").hide();
                $("#avbryt").hide();

                $("#kjop").show();

                Array.from(cell.parentElement.getElementsByTagName("th")).forEach((x)=>{
                    if(x != cell){
                        x.style.backgroundColor = "cornflowerblue";
                        x.setAttribute("data-fargeTilstand",1);
                        x.getElementsByClassName("fa fa-arrow-down")[0].style.display   = "none";
                        x.getElementsByClassName("fa fa-arrow-up")[0].style.display = "none";
                    }


                });

                if(cell.getAttribute("data-fargeTilstand") == 0){

                    cell.setAttribute("data-fargeTilstand",1);

                    cell.style.backgroundColor ="lightseagreen";
                    cell.getElementsByClassName("fa fa-arrow-up")[0].style.display  = "none";
                    cell.getElementsByClassName("fa fa-arrow-down")[0].style.display = "inline";
                }
                else {
                    cell.style.backgroundColor ="lightseagreen";
                    cell.setAttribute("data-fargeTilstand",0);
                    cell.getElementsByClassName("fa fa-arrow-down")[0].style.display   = "none";
                    cell.getElementsByClassName("fa fa-arrow-up")[0].style.display = "inline";
                }


            },(error)=>{
                $("#infoBoks").show();
                $("#infoBoks").text("Problemer med tilkobling!");
                setTimeout(() => {  CommunicationModule.sorterBestillinger(colNr,cell); }, 10000);

            });
        },
        slettBestilling: function(element){
            if(sessionStorage.getItem("innlogget") == null) return;
            let cb = slettBestillingCallback1;
            let next = slettPBestilling.bind(this);

            preHookSlettBestilling(cb,next,element).then((x)=>{
                StorageModule.setViewTo(Number(sessionStorage.getItem("viewTo"))-1);
                $("#spinner2").hide();
                $("#placeHolderTable").show();
                $("#getMore").css("display","block");
                $("#infoBoks").hide()
                $("#slettButton").show();
                $("#slettButton").prop( "disabled", false);
                $("#kjop").prop( "disabled", false);

            },(error)=>{
                $("#infoBoks").show();
                $("#infoBoks").text("Problemer med tilkobling!");
                setTimeout(() => {  CommunicationModule.slettBestilling(element); }, 10000);

            });
        }
    }
})();

var StorageModule = (function(){

    function setPViewTo(viewTo){
        sessionStorage.setItem("viewTo",viewTo);
    }

    function removePBestilling(id){
        let array = JSON.parse(sessionStorage.getItem("bestillinger"));
        array.splice(id,1);
        sessionStorage.setItem("bestillinger",JSON.stringify(array));
    }
    function setPChangeTime(change){

        sessionStorage.setItem("changeDateTime",change);
    }

    function getPBestillinger(){


        if(sessionStorage.getItem('bestillinger') != null){
            let rowsAdded = 0;
            JSON.parse(sessionStorage.getItem('bestillinger')).forEach((x)=>{ GUIModule.addBestilling(x,true); rowsAdded++;});
            StorageModule.setViewTo(rowsAdded);
            return;
        }

        $("#slettButton").hide();
        StorageModule.setViewTo(0);
        CommunicationModule.getMoreBestillinger(0,10);

    }



    function getPFilmValg(){
        if(sessionStorage.getItem('filmValg') != null){
            GUIModule.initFilmValg(JSON.parse(sessionStorage.getItem('filmValg')));
            return;
        }
        CommunicationModule.getFilmValg();
    }

    function setPFilmValg(filmValg){
        sessionStorage.setItem('filmValg',JSON.stringify(filmValg));
    }

    function endrePBestilling(id,postBestilling){
        let array = JSON.parse(sessionStorage.getItem("bestillinger"));
        array.splice(id,1,postBestilling);
        sessionStorage.setItem("bestillinger",JSON.stringify(array));

    }
    function addPBestilling(bestilling){

        let bestillinger = [];


        if(sessionStorage.getItem('bestillinger') != null){
            bestillinger = JSON.parse(sessionStorage.getItem('bestillinger'));
        }

        bestillinger.push(bestilling);
        sessionStorage.setItem('bestillinger',JSON.stringify(bestillinger));

    }
    function slettPBestillinger(){
        sessionStorage.removeItem('bestillinger');
    }
    return {
        getBestillinger : function (){
            getPBestillinger();
        },
        addBestilling:  function (bestilling){
            addPBestilling(bestilling);
        },
        slettBestillinger:  function(){
            slettPBestillinger();
        },
        setViewTo: function(viewTo){
            setPViewTo(viewTo);
        },
        setFilmValg: function(filmValg){
            setPFilmValg(filmValg);
        },
        getFilmValg : function(){
            getPFilmValg();
        },
        setChangeTime: function(timeChange){
            setPChangeTime(timeChange);
        },

        removeBestilling: function(id){
            removePBestilling(id);
        },

        endreBestilling: function(rowIndex,postBestilling){
            endrePBestilling(rowIndex,postBestilling);
        }
    }
})();

//Module pattern. Alt i return er public, og alt som ikke er i return er private.
//Er alternativet til bruk av klasse.

var GUIModule = (function (){
        /**
         *  Globale variabler, ettersom det skal utrykkes at variablenet
         *  er konfigurerbare og dermed skal være lett tilgjengelig.
         *
         *  Fungerer i module pattern som private variabler.
         *
         */

        let tableHeader = ["Film","Antall","Fornavn","Etternavn","Telefon","Epost","           ","           "];
        let filmTitles  = [];


        let tempElement = null;

        let minLenAntall  = 1;
        let maxLenAntall  = 4; //Kan være stor-innkjøp til bedrift eller noe.
        let patternAntall = /^[1-9][0-9]*$/;  //Å bestill 0 antall filmer gir ikke mening.
        let requiredAntall = true;
        let errorMessageEmptyAntall = "Må skrive noe inn i antall";
        let errorMessageAntall = "Antal skal kun bestå av tall";
        let errorMessageAntallLengde = "Antall må ha mellom " + minLenAntall + " og " + maxLenAntall + " tall";


        let maxLenFornavn = 15;
        let minLenFornavn = 1;
        let requiredFornavn = true;
        let patternFornavn = /^[a-zA-Z]+$/;
        let errorMessageEmptyFornavn = "Må skrive noe inn i fornavnet";
        let errorMessageFornavn = "Fornavn skal kun bestå av bokstaver";
        let errorMessageFornavnLengde = "Antall må ha mellom " + minLenFornavn + " og " + maxLenFornavn + " bokstaver";

        let maxLenBrukernavn = 15;
        let minLenBrukernavn = 6;
        let requiredBrukernavn = true;
        let patternBrukernavn = /^[a-zA-Z0-9]+$/;
        let errorMessageEmptyBrukernavn = "Må skrive noe inn i brukernavnet";
        let errorMessageBrukernavn= "Brukernavn skal kun bestå av bokstaver og tall";
        let errorMessageBrukernavnLengde = "Brukernavnet må ha mellom " + minLenBrukernavn + " og " + maxLenBrukernavn + " bokstaver og tall";

        let maxLenPassord= 25;
        let minLenPassord = 8;
        let requiredPassord = true;
        let errorMessageEmptyPassord = "Må skrive noe inn i passordet";
        let errorMessagePassordLengde = "Passordet må ha mellom " + minLenPassord + " og " + maxLenPassord + " tegn";


        let maxLenEtternavn = 15;
        let minLenEtternavn = 1;
        let requiredEtternavn = true;
        let patternEtternavn  = /^[a-zA-Z]+$/;
        let errorMessageEmptyEtternavn = "Må skrive noe inn i etternavnet";
        let errorMessageEtternavn = "Etternavn skal kun bestå av bokstaver";
        let errorMessageEtternavnLengde = "Antall må ha mellom " + minLenEtternavn + " og " + maxLenEtternavn + " bokstaver";


        let requiredEpost = true;
        let maxLenEpost   = 25; //Email som er relevant for mennesker krever at en taster inn.
        let minLenEpost   = 3; // F.eks t@a . En trenger ikke . i email, pga finne sjeldne tilfeller der domenet er top domenet.
        let errorMessageEmptyEpost = "Må skrive noe inn i epost";
        let errorMessageEpost = "Epost skal være på formen xxx@ttt, pp@tt.com, ppt@tt.c.com, pp.t@tt.c ...etc";
        let errorMessageEpostLengde = "Epost må ha mellom " + minLenEpost + " og " + maxLenEpost + " tall";

        /**
         * Regler fra wikipedia (https://en.wikipedia.org/wiki/Email_address)
         *
         * "
         *      Local part:
         *      -----------
         *
         *      1. uppercase and lowercase Latin letters A to Z and a to z
         2. digits 0 to 9
         3. printable characters !#$%&'*+-/=?^_`{|}~
         dot ., provided that it is not the first or last character and provided also that it does not appear consecutively (e.g., John..Doe@example.com is not allowed).[5]"
         *
         *      Domain part:
         *      ------------
         *      1. uppercase and lowercase Latin letters A to Z and a to z;
         *      2. digits 0 to 9, provided that top-level domain names are not all-numeric;
         *         hyphen -, provided that it is not the first or last character."
         *
         *
         * @type {RegExp}
         */
        let patternEpost  = /^[a-zA-Z0-9!#$%&'*+/=?^_`{|}~-]+(\.[a-zA-Z0-9!#$%&'*+/=?^_`{|}~-]+)*@(([a-zA-Z]+)|([a-zA-z0-9]+[a-zA-z]+)|([a-zA-z]+[a-zA-Z0-9]+))((.|[a-zA-Z0-9-]+)([a-zA-Z0-9])+)*$/;

        let requiredTelefon = true;
        let maxLenTelefon   = 15;
        let minLenTelefon   = 5;
        let patternTelefon  = /^(\+[0-9]+)?[0-9]+$/;
        let errorMessageEmptyTelefon = "Må skrive noe inn i telefonnr";
        let errorMessageTelefon = "Ugyldig telefonr. Merk at landskode(f.eks +47) etterfulgt av telefonnr er lov.";
        let errorMessageTelefonLengde = "Telefon må ha mellom " + minLenTelefon + " og " + maxLenTelefon + " tall, hvor bruk av tegnet + er lov i begynnelsen";



        function deletePTable(){
            let table = $("#placeHolderTable > table")[0];

            if(table != null) {
                //For å hindre at headeren i tabellen blir slettet
                let tableHeader = table.firstChild;
                //Slett alle rader i tabellen
                while (table.firstChild) {
                    table.removeChild(table.firstChild);
                }
                //Legg tilbake headeren til tabellen
                table.appendChild(tableHeader);

            }
        }


        /**
         *  Valider informasjon:
         * @param antall Antall filmer
         * @returns {boolean} informasjonen er gyldig?
         */
        function validateAntall(antall){

            let errorPlaceholderAntall    = $("#errorAntall > p");

            if(antall.length == 0){

                if(!requiredAntall) return true;
                errorPlaceholderAntall.text(errorMessageEmptyAntall);
                $("#antall").css("background-color","lightcoral");
                return false;
            }

            let len = antall.length;
            if(len < minLenAntall || len > maxLenAntall){
                errorPlaceholderAntall.text(errorMessageAntallLengde);
                $("#antall").css("background-color","lightcoral");
                return false;
            }
            if(!patternAntall.test(antall)){
                errorPlaceholderAntall.text(errorMessageAntall);
                $("#antall").css("background-color","lightcoral");
                return false;
            }
            errorPlaceholderAntall.text("");

            $("#antall").css("background-color","lightgreen");
            return true;
        };

        /**
         * Valider informasjon
         * @param fornavn Fornavn til en person
         * @returns {boolean} Informasjonen er gyldig?
         */
        function validateFornavn (fornavn) {

            let errorPlaceholderFornavn   = $("#errorFornavn > p");



            if(fornavn.length == 0){

                if(!requiredFornavn) return true;
                errorPlaceholderFornavn.text(errorMessageEmptyFornavn);

                $("#fornavn").css("background-color","lightcoral");

                return false;
            }
            let len = fornavn.length;

            if(len < minLenFornavn || len > maxLenFornavn){

                errorPlaceholderFornavn.text(errorMessageFornavnLengde);
                $("#fornavn").css("background-color","lightcoral");

                return false;
            }


            if(!patternFornavn.test(fornavn)){

                errorPlaceholderFornavn.text(errorMessageFornavn);
                $("#fornavn").css("background-color","lightcoral");

                return false;
            }
            errorPlaceholderFornavn.text("");

            $("#fornavn").css("background-color","lightgreen");

            return true;


        };

        /**
         * Valider informasjon
         * @param etternavn Etternavn til en person
         * @returns {boolean} Informasjonen er gyldig?
         */
        function validateEtternavn(etternavn){

            let errorPlaceholderEtternavn = $("#errorEtternavn > p");



            if(etternavn.length == 0){
                if(!requiredEtternavn) return true;
                errorPlaceholderEtternavn.text(errorMessageEmptyEtternavn);
                $("#etternavn").css("background-color","lightcoral");
                return false;
            }
            let len = etternavn.length;

            if(len < minLenEtternavn || len > maxLenEtternavn){
                errorPlaceholderEtternavn.text(errorMessageEtternavnLengde);
                $("#etternavn").css("background-color","lightcoral");
                return false;
            }

            if(!patternEtternavn.test(etternavn)){
                errorPlaceholderEtternavn.text(errorMessageEtternavn);
                $("#etternavn").css("background-color","lightcoral");
                return false;
            }
            errorPlaceholderEtternavn.text("");
            $("#etternavn").css("background-color","lightgreen");
            return true;
        };

        /**
         * Valider informasjonen
         * @param telefon  Telefonnr til personen
         * @returns {boolean} Informasjonen er gyldig?
         */
        function validateTelefon(telefon){

            let errorPlaceholderTelefon   = $("#errorTelefon > p");



            if(telefon.length == 0){
                if(!requiredTelefon) return true;
                errorPlaceholderTelefon.text(errorMessageEmptyTelefon);
                $("#telefon").css("background-color","lightcoral");
                return false;
            }
            let len = telefon.length;

            if(len < minLenTelefon || len > maxLenTelefon){
                errorPlaceholderTelefon.text(errorMessageTelefonLengde);
                $("#telefon").css("background-color","lightcoral");
                return false;
            }


            if(!patternTelefon.test(telefon)){
                errorPlaceholderTelefon.text(errorMessageTelefon);
                $("#telefon").css("background-color","lightcoral");
                return false;
            }


            errorPlaceholderTelefon.text("");
            $("#telefon").css("background-color","lightgreen");
            return true;
        };


        function validateBrukernavn(brukernavn){
            let errorPlaceholderBrukernavn     = $("#errorBrukernavn > p");

            if(brukernavn.length == 0){

                if(!requiredBrukernavn) return true;
                errorPlaceholderBrukernavn.text(errorMessageEmptyBrukernavn);

                $("#brukernavn").css("background-color","lightcoral");

                return false;
            }
            let len = brukernavn.length;

            if(len < minLenBrukernavn || len > maxLenBrukernavn){

                errorPlaceholderBrukernavn.text(errorMessageBrukernavnLengde);
                $("#brukernavn").css("background-color","lightcoral");

                return false;
            }


            if(!patternBrukernavn.test(brukernavn)){

                errorPlaceholderBrukernavn.text(errorMessageBrukernavn);
                $("#brukernavn").css("background-color","lightcoral");

                return false;
            }
            errorPlaceholderBrukernavn.text("");

            $("#brukernavn").css("background-color","lightgreen");

            return true;
        }
        function validatePassord(passord){
            let errorPlaceholderPassord     = $("#errorPassord > p");

            if(passord.length == 0){

                if(!requiredPassord) return true;
                errorPlaceholderPassord.text(errorMessageEmptyPassord);

                $("#passord").css("background-color","lightcoral");

                return false;
            }
            let len = passord.length;

            if(len < minLenPassord || len > maxLenPassord){

                errorPlaceholderPassord.text(errorMessagePassordLengde);
                $("#passord").css("background-color","lightcoral");

                return false;
            }


            errorPlaceholderPassord.text("");

            $("#passord").css("background-color","lightgreen");

            return true;
        }
        /**
         * Valider informasjonen
         * @param epost Epost til personen
         * @returns {boolean}
         */
        function validateEpost(epost){

            let errorPlaceholderEpost     = $("#errorEpost > p");

            if(epost.length == 0){
                if(!requiredEpost) return true;
                errorPlaceholderEpost.text(errorMessageEmptyEpost);
                $("#epost").css("background-color","lightcoral");
                return false;
            }

            let len = epost.length;
            if(len < minLenEpost || len > maxLenEpost){
                errorPlaceholderEpost.text(errorMessageEpostLengde);
                $("#epost").css("background-color","lightcoral");
                return false;
            }


            //Hvis vi ikke har akkurat en @, så er eposten ugyldig.
            if(Array.from(epost).filter(x=>x=='@').length != 1){
                errorPlaceholderEpost.text(errorMessageEpost);
                $("#epost").css("background-color","lightcoral");
                return false;

            }

            //Vanskeligheter med å få til eksakt match med tanke på @.
            //Dvs er en bug der slik at en kan få to @ inni der pga
            //den ikke ser etter exact match. Vet en bruker ^ og $ for dette,
            //men feiler. Har sjekk før denne med filter for å sørge for at en kun kan ha 1 @,slik at det
            // ikke er delvis matching mulig, og at en dermed får exact match
            if(!patternEpost.test(epost)){
                errorPlaceholderEpost.text(errorMessageEpost);
                $("#epost").css("background-color","lightcoral");
                return false;
            }

            errorPlaceholderEpost.text("");
            $("#epost").css("background-color","lightgreen");
            return true;
        };


        /**
         * Konstruer tabell header, dvs <th> til en tabell
         * @param tableHeader Informasjon fra bestillingsskjema
         * @returns {HTMLTableRowElement}
         */
        function constructNewTableHeader(tableHeader){

            let rowHeader = document.createElement("tr");

            tableHeader.forEach((item,index)=>{

                let cell = document.createElement("th");
                cell.innerText = tableHeader[index];
                cell.style.visibility = "hidden";
                cell.addEventListener("click",(e)=>{

                    let indexVal = index;
                    $("#slettButton").hide();
                    $("#getMore").hide();
                    CommunicationModule.sorterBestillinger(indexVal,e.currentTarget);



                });

                let arrowDown = document.createElement("i");
                arrowDown.className = "fa fa-arrow-down";
                arrowDown.style.display = "none";

                cell.appendChild(arrowDown);

                let arrowUp = document.createElement("i");
                arrowUp.className = "fa fa-arrow-up";
                arrowUp.style.display = "none";
                cell.appendChild(arrowUp);

                cell.setAttribute("data-fargeTilstand",0);

                rowHeader.appendChild(cell);

                //Skal være sortert etter etternavn i starten
                if(cell.innerText=="Etternavn"){
                    cell.click();
                }
            });
            return rowHeader;
        }

        function bestillingsDataToArray(bestillingsData){

            return   [bestillingsData.filmValg,bestillingsData.antall,
                bestillingsData.fornavn,bestillingsData.etternavn,
                bestillingsData.telefon,bestillingsData.epost];


        }


        function constructNewRowRecordWrapper(bestillingsData){
            return constructNewRow(bestillingsDataToArray(bestillingsData));
        }

        /**
         * Konstruer en ny rad til en tabell basert på informasjon fra filmData.
         * @param bestillingsData
         * @returns {HTMLTableRowElement}
         */
        function constructNewRow(bestillingsData){

            let row = document.createElement("tr");

            bestillingsData.forEach((item,index)=>{
                let cell = document.createElement("td");
                cell.innerText = bestillingsData[index];

                row.appendChild(cell);
            });


            let slettButton = document.createElement("button");
            slettButton.innerText = "Slett bilett";
            slettButton.className = "slettButton";
            slettButton.addEventListener("click",(e)=>{
                $("#slettButton").hide();
                CommunicationModule.slettBestilling(e.currentTarget.parentElement.parentElement);


            });
            let cell = document.createElement("td");
            cell.appendChild(slettButton);

            row.appendChild(cell);

            let endreButton = document.createElement("button");
            endreButton.innerText = "Endre bilett";
            endreButton.className = "endreButton";
            endreButton.addEventListener("click",(e)=>{

                let preElement = GUIModule.getTempElement();
                if(preElement != null){
                    let rowIndex = preElement.rowIndex-1;
                    if(rowIndex % 2 == 0) preElement.style.backgroundColor = "lightgray";
                    else                  preElement.style.backgroundColor = "white";
                }
                let element = e.currentTarget.parentElement.parentElement;
                element.style.backgroundColor ="lightseagreen";
                GUIModule.endreExtractBillett(element);


            });

            cell = document.createElement("td");
            cell.appendChild(endreButton);

            row.appendChild(cell);

            return row;
        }

        function slettPBestilling(element){
            element.parentElement.removeChild(element);
        }
        /**
         * Konstruer ett option element
         * @param val
         * @param index
         * @returns {HTMLOptionElement}
         */
        function  constructOption(val,index){
            let option = document.createElement("option");
            option.value = index;
            option.innerText = val;

            return option;
        }

        /**
         * Konstruer option element og legg dem inn i en select meny.
         * @param filmTitles
         * @param filmList
         */
        function constructAndAddOptions(filmTitles,filmList){
            filmTitles.forEach((val,idx) => filmList.append(constructOption(val,idx)));
        }

        /**
         *  Lager en ny tabell hvis den ikke finnes.
         *  Lgger til en ny rad basert på bestillingsData hvis isValid er true.
         * @param bestillingsData Informasjon fra bestillings skjema
         * @param isValid Hvorvidt bestillingsskjemaet er gyldig
         */

        function updateOrCreateTable(bestillingsData,isValid) {

            let table = $("#placeHolderTable > table")[0];

            if(table == null){
                table = document.createElement("table");


                let headerRow = constructNewTableHeader(tableHeader);
                table.appendChild(headerRow);

                let placeHolder = $("#placeHolderTable")[0];
                placeHolder.appendChild(table);
            }

            if(isValid){
                let row = constructNewRowRecordWrapper(bestillingsData);
                table.appendChild(row);
            }

        }

        /**
         *  Validerer informasjonen fra bestillings skjemaet
         * @param bestillingsData Informasjon fra bestillings skjemaet
         * @returns {boolean} true hvis bestillings skjemaet er gyldig
         */
        function validateChain(bestillingsData){

            //Må gjøre det slikt med temp variabel, og ikke &&= med bare isValid
            //for ellers blir ikke alt vurdert.
            let validTemp = true;
            let isValid   = true;


            validTemp =  validateAntall(bestillingsData.antall);

            isValid &&= validTemp;

            validTemp = validateFornavn(bestillingsData.fornavn);

            isValid &&= validTemp;

            validTemp = validateEtternavn(bestillingsData.etternavn);

            isValid &&= validTemp;

            validTemp =  validateTelefon(bestillingsData.telefon);

            isValid &&=  validTemp;

            validTemp = validateEpost(bestillingsData.epost);

            isValid &&= validTemp;

            return isValid;
        }

        function validateLoginChain(loginData){

            //Må gjøre det slikt med temp variabel, og ikke &&= med bare isValid
            //for ellers blir ikke alt vurdert.
            let validTemp = true;
            let isValid   = true;


            validTemp =  validateBrukernavn(loginData.brukernavn);

            isValid &&= validTemp;

            validTemp = validatePassord(loginData.passord);

            isValid &&= validTemp;


            return isValid;
        }
        return {
            /**
             * Ekstraherer bestillingen ut av et bestillings skjema
             */
            extractBestilling: function(){
                let filmList = $("#filmValg")[0];


                let filmValg = filmTitles[filmList.options[filmList.selectedIndex].value];

                let bestillingsData = {

                    filmValg: filmValg,
                    antall:   $("#antall").val().trim(),
                    fornavn:  $("#fornavn").val().trim(),
                    etternavn: $("#etternavn").val().trim(),
                    telefon: $("#telefon").val().trim(),
                    epost: $("#epost").val().trim()

                }

                return bestillingsData;

            },
            extractLogin: function(){

                let loginData = {

                    brukernavn:   $("#brukernavn").val().trim(),
                    passord:      $("#passord").val().trim()

                }

                return loginData;

            },
            endreExtractBillett: function(element) {

                let columns  =  element.getElementsByTagName("td");

                filmTitles.some((val,idx)=> {
                    if(val == columns[0].innerText){
                        $("#filmValg").val(idx);
                        return true;
                    }

                    else return false;
                });

                $("#antall").val(columns[1].innerText);
                $("#fornavn").val(columns[2].innerText);
                $("#etternavn").val(columns[3].innerText);
                $("#telefon").val(columns[4].innerText);
                $("#epost").val(columns[5].innerText);


                tempElement = element;

                $("#endre").show();
                $("#avbryt").show();
                $("#kjop").hide();

            },
            /**
             * Legger til en ny bestilling i tabellen
             *
             * @param bestillingsData Informasjon assosiert med bestillingen
             * @param isValid
             */
            addBestilling: function(bestillingsData,isValid){
                updateOrCreateTable(bestillingsData,isValid);
            },
            /**
             * Valider bestillings skjemaet
             * @param bestillingsData Informasjon fra bestillings skjema
             * @returns {boolean} true hvis bestillings skjemaet er gyldig2
             */
            validate: function (bestillingsData) {

                return validateChain(bestillingsData);
            },
            validateLogin: function (loginData) {

                return validateLoginChain(loginData);
            },
            /**
             * Initialiserer registererings form
             */
            initFilmValg: function(filmTitlesParam){
                let filmList =  $("#filmValg");
                constructAndAddOptions(filmTitlesParam,filmList);
                filmTitles = filmTitlesParam;
                return;

            },
            deleteTable: function (){
                deletePTable();
                return;
            },
            slettBestilling: function(element){
                slettPBestilling(element);
            },

            endreBestilling: function(element,postBestilling){

                let columns  =  element.getElementsByTagName("td");

                columns[0].innerText = postBestilling.filmValg;
                columns[1].innerText = postBestilling.antall;
                columns[2].innerText = postBestilling.fornavn;
                columns[3].innerText = postBestilling.etternavn;
                columns[4].innerText = postBestilling.telefon;
                columns[5].innerText = postBestilling.epost;

            },
            getTempElement: function(){
                return tempElement;
            },
            closeLoginWindow(){
                $("#loginOk").prop("disabled",true);
                $("#login").hide();
            },

            openLoginWindow(){


                $("#login").show();
            }



        }
    }
)();

