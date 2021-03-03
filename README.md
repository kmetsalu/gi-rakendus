# geenivaramu rakendus

### Kasutatud tehnoloogiad

Proovitöö rakendusserver on kirjutatud Java keeles (Spring raamistikuga), kasutajaliides on kirjutatud Javascriptis/Vue.js raamistikus ning andmebaasina on kasutatud Postgres-i.

Täpsemalt kasutatud tähtsamad tehnoloogiad:
- Java 8
- Spring Boot
- Spring Security
- JWT
- Postgres
- Hibernate
- Javascript
- Vue.js
- Vuex
- Vuetify
- OpenCSV
- Axios

### Ülesanded

Lahendasin ülesande raames nõuete kohaselt ära kõik etteantud punktid ning lisaks ka boonusülesanded. Sisuliselt kirjutasin ma rakenduse kus saab endale luua kasutaja ning sellega rakendusse sisse logida (ilma sisse logimata ei saa millelegi ligi). Rakendus koosneb kolmest suuremast alamosast - andmete otsimine, failide üleslaadimine ning kasutajate haldamine. 

Andmete otsimise puhul on otsingukast kuhu saab kirjutada kas isikukoodi täielikul kujul või siis osaliselt. Osalise otsimise puhul tuleb otsingu lõppu lisada * (*wildcard*) sümbol nagu näiteks 392*. Eduka otsingu korral kuvatakse kasutajale andmetabel mis on mugavuse eesmärgil jagatud 15-elemendi kaupa lehtedeks ning tabelit on võimalik kõigi tunnuste järgi sorteerida. Lisaks veel .csv failidest etteantud väljadele on tabelis iga visiidil käinud inimese sünnikuupäev, sugu ning vanus visiidil käimise ajal.

Failide üleslaadimise puhul saab korraga ette anda ühe faili mis seejärel serveris valideeritakse ning töödeldakse. Programm on võimeline hakkama saama ka failidega kus tunnused on suvalises järjestuses või tunnuste nimed võivad teataval määral varieeruda nagu testfailides ette antud. Lisaks kontrollib programm kas etteantud isikukood on realistlik või mitte. 

Viimane suurem osa on kasutajate haldamine. Rakenduse puhul on võimalik neli erinevat rolli - juurkasutaja (*root*), otsinguõigusega kasutaja, failide üleslaadimise õigusega kasutaja ning teiste kasutajate haldamise õigusega kasutaja. Kui on teiste kasutajate haldamise õigus, siis saad teiste puhul neid rolle muuta. Kuna proovisin võimalikult jäljendada seda kuidas pärismaailmas asjad üldiselt töötavad, siis kasutaja ei saa muuta iseenda õiguseid ning ka juurkasutaja õiguseid. Juurkasutaja on kasutajarollide erijuht mis saab rakenduses kõike teha ning kelle õigusi teised kuidagi muuta ei saa. Lisaks lihtsalt kasutajate muutmisele on juurkasutaja ainukene grupp kes saab teiste kasutajate kontosid lukku panna/lahti teha. Jällegi tundus loogiline, et täitsa iga tavakasutaja seda teha ei tohiks kellel kasutajate muutmise õigus olemas on. Juurkasutaja genereeritakse rakendust käima pannes ning hiljem seda sorti kasutajaid juurde teha/vähemaks võtta saab vaid läbi otse andmebaasi päringute tegemise.

Lisaks kui kasutaja pole kas sisse logitud või tal puudub vastav õigus mõnda rakenduse lehte näha, siis ta sinna ligi ei saa vaid rakendus annab vastava veateate ning hoolitsesin ka selle eest, et näiteks läbi Postman-i otse *API endpoint*-ide pihta päringuid ei saaks teha kui kasutaja vastavat õigust ei oma.

### Rakenduse käima panemine

Rakendus koosneb kahest suuremast osast - *backend* ja *frontend*. Et rakendus korrektselt käima saada tuleb teha järgnevad sammud:
- **Minna backend kasuta** ja **pom.xml kaudu laadida alla vajaminevad lisad** (IDE võib seda ka automaatselt teha)
 ![Maven install](https://i.imgur.com/U1yoDUT.png)
- Jooksutada käsurealt **mvn clean install -DskipTests**
- Käsuralt **cd target** ning seejärel **java -jar backend-0.0.1-SNAPSHOT.jar**

Sellega nüüd *backend* töötab. Kliendi käima saamiseks:
- **Minna frontend kausta** ja teha käsurealt **npm install**
- Kui see on tehtud, siis käsurealt **npm run serve**
- Kui kood on kompileerumise lõpetanud, siis saab töötavale rakendusele ligi **http://localhost:8081/**

Andmebaas luuakse rakendusega käivitamisega automaatselt ning sinna lisatakse ka kaks kasutajat:

Kasutaja nimega **root** mille parool on **admin**

Kasutaja nimega **user** mille parool on **password**

Kasutajal root on juurõigused ehk saab rakenduses absoluutselt kõike teha ning kasutajal user on vaid otsimise õigus. Lisaks saab ka registreerida uusi kasutajaid ning nendega rakendust kasutada. Samas otsitavaid andmeid tööle paneku käigus automaatselt andmebaasi ei loeta vaid need tuleb käsitsi laadida.

Kui soovida rakenduse töö lõpetada, siis mõlemal käsureal (*frontend* ja *backend*) kasutada kombinatsiooni ctrl-c. 

### Tehnilised aspektid

Failide üleslaadimine/töötlemine on muidu rakenduses väga sujuv, aga kõige suurema 1.2GB suuruse faili puhul läks vähemalt minul enda arvuti peal ~75 minutit (võrdluseks väikesed 10000 reaga failid ~2-3 sekundit). Ma ei tea kas see on 10 millioni rea kohta vähe või palju kuid kindlasti saaks paremini. Samas oleks ilmselt mingi *batch* süsteemi implementeerimine antud juhul liiga palju aega võtnud ning võrreldes muuga polnud see niivõrd prioriteet. Ka hetkeline lahendus töötab hästi, aga suurte failide puhul tuleb lihtsalt kaua oodata. Kui peaks juhtuma, et selle suure faili puhul JVM hakkab *OutOfMemory* või *Heap* veateateid andma ja ei lae lõpuni, siis anda arenduskeskkonnas VM argumentideks näiteks -XX:-UseGCOverheadLimit -Xmx16384m kindla varuga ette anda ja siis peaks töötama küll.

Proovisin asja ka Dockeriga tööle saada, aga see tekitas ettenägematuid kõrvalefekte. Kuna panen ka vastava docker-compose.yml kaasa saab seda ning Dockerfile faile uurida (NB! kui Dockeri kaudu käima panna, siis ennem application.properties failis **spring.datasource.url=jdbc:postgresql://localhost:5432/postgres** muuta **spring.datasource.url=jdbc:postgresql://postgres:5432/postgres**, sest Dockerile ei meeldi kui *hostname* on *localhost* ja kui jälle käsurealt käivitada, siis tagasi muuta). Samas kui soov on Dockeri kaudu jooksutada, siis teha seda **start.sh** faili kaudu mis kõik vajalikud sammud automaatselt ära teeb (samas enne *Dockerfile* failides *path*-id üle vaadata, sest need võivad repositooriumi kloonimisega katki minna). Rakendus läheb küll käima, ja töötab nii nagu peaks kui lihtsalt ringi navigeerida ja asju teha, aga lehti *refresh*-ides või URL-idele käsitsi minnes annab 404 veateate. See on üsna levinud probleem kui *single-page application*-eid dokeriseerima hakata ([https://router.vuejs.org/guide/essentials/history-mode.html]), aga vähemalt seni pole mul õnnestunud Apache ega Nginx konfiguratsioone Dockeri keskkonnas tööle saada. Lisaks veel failide üleslaadimise puhul eraldatakse Dockeri konteineritele liiga vähe mälu, et oleksin saanud kõige suurema faili üles laadida ja vähemalt seni pole mul õnnestunud seda mälumahtu suurendada ei eelpool mainitud argumendid ette andes, ega Dockeri seadetest suurendades.

Lisaks on määratud appication.protirties failis spring.jpa.hibernate.ddl-auto=create ehk siis iga kord rakendus uuesti käima pannes tehakse andmebaas puhtaks ning laetakse uuesti sisse esialgsed andmed ehk siis seal on vaid eelpoolmainitud kaks kasutajakontot ja ei midagi muud. Kui soovida, et andmed läbi käimapanekute alles jääks saab kasutada *create* asemel *update*, aga kui plaanis on mu lahendust testida, siis tundus mõistlikum *create* peale jätta.

Rakenduse testimiseks kasutasin peamiselt *Postman* rakendust ning lihtsalt kõikvõimalike erinevate situatsioonide läbi katsetamist.

### Ajakulu
- Ülesannete 1 ja 2 rakendusserveri osa ~2h
- Ülesannete 1 ja 2 kliendi osa ~4h
- Boonusülesande rakendusserveri osa ~8h
- Boonusülesande kliendi osa ~6h
- Koodi silumine (refaktoriseerimine, CSS, äärejuhtude sisse kirjutamine, testimine) ~8h
- Dockeriga rakenduse korreltselt tööle saamise üritamine ~6h
- Töö dokumenteerimine ~2h

Kokku ~35-40 tundi
