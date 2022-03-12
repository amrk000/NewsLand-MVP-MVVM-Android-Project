<div align="center">
<img src="https://user-images.githubusercontent.com/63168118/157975455-9c5499e2-10ef-4ceb-ab7a-cde268aff15d.png" />
</div>

<h2>NewsLand Android App Project aims to be a good sample for learners and reference for developers which provides:</h2>
<ul>
<li>
<h3>Android Architectural Patterns (MVP - MVVM)</h3>
</li>
<li>
<h3>Dependency Injection (Dagger2)</h3>
</li>
<li>
<h3>Working with observers & observables (LiveData - RxJava)</h3>
</li>
        
</br>

<div align="center">
<table>
<tr>
<td width="auto">
        
<h2>App Features:</h2>
<ul>
<li>Get Latest News Headlines</li>
<li>Read & Share news you like</li>
<li>See news in multiple languages & countries</li>
<li>Filter news to match your interrests using categories</li>
</ul>

<h2>Implementation Highlights:</h2>
<ul>
  <li>Rest API - Data Source: <a href="https://newsapi.org/">newsapi.org</a></li>
  <li>Fetched & Parsed data with Retrofit + Gson + Glide</li>
  <li>Blocking News with Room Persistence</li>
  <li>Infinity Scrolling (Lazy Load) RecyclerView</li>
  <li>MultiView (ViewHolder) RecyclerView Adapter</li>
  <li>Realtime Network Checker with BoradcastReciever</li>
  <li>Using & Handling WebView</li>
  <li>ViewBinding android views</li>
  <li>Implemented in Multiple Software Architectural Patterns</li>
  <li>Di with Dagger2 (MVVM + DI)</li>
  <li>Injecting ViewModels with Dagger 2 using Factory (MVVM + DI)</li>
  <li>LiveData (MVVM)</li>
  <li>RxJava (MVP)</li>
  <li>UI Components:<br>Chips, BottomSheet, RecyclerView, ListView, Swipe Refrech Layout, Snackbar</li>
</ul>
        <br>
      <div align="center">
  <a href="https://dl.dropbox.com/s/paz3ry2bw9zlwbe/NewsLand.apk">
    <img width="200px" src="https://user-images.githubusercontent.com/63168118/157979139-36dbcfe4-c82d-43b9-85d3-0e45eeba05d9.png"/>
  </a>
  </div>    
<td>
        
<td width="auto">
<div align="center"><video src="https://user-images.githubusercontent.com/63168118/158020353-eef7cc82-b5bb-4d77-bf26-16182b56f190.mp4"></div>
</td>
        
</tr>
</table>
</div>

<hr>

<h2>Android Architectural Patterns:</h2>
<h4>This Project is Implemented in Multiple Software Architectural Patterns using multiple tools to be a good sample for learner and reference for developer.</h4>
<ul>
<li>MVC (Skipped): No longer used as it's bad for android.</li>
<li>MVP (Good):</li>
  <ul>
    <li><a href="https://github.com/amrk000/NewsLand-MVP-MVVM-Android-Project/tree/master/NewsLand%20MVP">MVP Version</a></li>
    <li><a href="https://github.com/amrk000/NewsLand-MVP-MVVM-Android-Project/tree/master/NewsLand%20MVP%20%2B%20DataManager">MVP + DataManager Version</a></li>
  </ul>
  <li>MVVM (Recommended):</li>
  <ul>
    <li><a href="https://github.com/amrk000/NewsLand-MVP-MVVM-Android-Project/tree/master/NewsLand%20MVVM">MVVM Version</a></li>
    <li><a href="https://github.com/amrk000/NewsLand-MVP-MVVM-Android-Project/tree/master/NewsLand%20MVVM%20%2B%20Dependency%20Injection">MVVM + Dependency Injection Version</a></li>
  </ul>
</ul>

</br>
<h2>Q&A:</h2>
<ul>
        
  <li>
  <h3>Why using Architectural Pattern instead of writing code in activity?</h3>
  <p>Architectural Patterns provide a better readability, modularity, scalability and maintainability so it's necessary to be used in projects that can scale and that have a team of developers working on it.<p>
  </li>
   
  <li>
  <h3>When not to use Architectural Pattern?</h3>
  <p>Thats's only possible in personal or small projects.<p>
  </li>
          
  <li>
  <h3>Which Architectural Pattern is better for android MVP or MVVM?</h3>
  <p>Both are good for android development but MVVM is the best choice right now and recommended by google. Although MVP is good too but it can't handle android lifecycle like MVVM.<p>
  </li>
          
  <li>
  <h3>MVVM Advantages over MVP?</h3>
  <p>less code as it doesn't depend on interfaces, Android lifecycle aware, Easier unit testing with dependency injection.<p>
  </li>
  
  <li>
  <h3>MVP Advantages over MVVM?</h3>
  <p>Easier to learn, Doesn't depend on google libraries, More flexible to new features & maintenance, Reactive programming is optional.<p>
  </li>
  
  <li>
  <h3>Why contract class isn't used in NewsLand MVP Version?</h3>
  <p>Contract class is optional and you can use it in MVP but i prefer letting interfaces separated.<p>
  </li>
  
  <li>
  <h3>What is DataManager in NewsLand MVP Version?</h3>
  <p>DataManager is an MVP extension layer which provides a clean API for accessing data from multiple sources and behaves like repository in MVVM.<p>
  </li>
   
  <li>
  <h3>Why ui folder/package isn't found in NewsLand MVVM Version?</h3>
  <p>It's there! but i prefer calling it view.<p>
  </li>
   
  <li>
  <h3>What are ViewModelFactory, ViewModelModule classes in NewsLand MVVM + DI Version?</h3>
  <p>These classes are used to be able to make viewmodels constructor injection using dagger2 as normal apporach can't be applied due to Dagger2 & ViewModels conflict.<p>
  </li>
  
  <li>
  <h3>Which is better LiveData or RxJava?</h3>
  <p>LivData is the best when it comes to UI & Android lifecycle integration like in ViewModels. RxJava is provides more options so it's better when working with Non-Ui tasks, threads and multi requests like chain REST API Requests.<p>
  </li>
        
  <li>
  <h3>Can LiveData and RxJava be used together?</h3>
  <p>Yes, as RxJava Provides stream conversion to LiveData.<p>
  </li>
        
  <li>
  <h3>What are BehaviorSubject, PublishSubject in RxJava?</h3>
  <p>They are RxJava observables that behave as LiveData Objects.<p>
  </li>
</ul>
