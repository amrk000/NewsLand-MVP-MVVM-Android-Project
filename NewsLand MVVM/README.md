<h1><a href="https://github.com/amrk000/NewsLand-MVP-MVVM-Android-Project">NewsLand Android Project</a> > MVVM</h1>
<div align="center">
<img src="https://user-images.githubusercontent.com/63168118/158036902-f9327886-1102-4eb0-96f7-6ebde6f1af37.png" />
</div>

</br>
<h1 align="center">Model View ViewModel Architectural Pattern</h1>

<ul>
        
  <li>
  <h3>Model (Data Model/Entity)</h3>
  <ul>  
    
  <li>
  <p>It's a class that holds data in single object which represents an entity.<p>
  </li>
   
  <li>
  <p>It containes variables, setters & getters methods only.<p>
  </li>  
    
  <li>
  <p>It shouldn't contain any logic or data request functions.<p>
  </li>  
    
  </ul>
  </li>
   
  <li>
  <h3>View (Activity & Fragment)</h3>
  <ul>  
    
  <li>
  <p>View is responsible for rendering UI and listening for user actions.<p>
  </li>
   
  <li>
  <p>It contains Views objects, views control methods, Event Listeners, View Adapters Initialization, Intent launching methods and Services/Broadcast Registeration.<p>
  </li>  
    
  <li>
  <p>It doesn't know about data models.<p>
  </li>
          
  <li>
  <p>It observes data changes which are provided by repository or manipulated by ViewModel.<p>
  </li> 
    
  </ul>
  </li>
          
  <li>
  <h3>ViewModel (Logic Class)</h3>
   <ul>  
    
  <li>
  <p>It's the middle layer between View & APP Data.<p>
  </li>
  
  <li>
  <p>View & ViewModel are linked together through methods & observables.<p>
  </li>
   
  <li>
  <p>It contains all logic which retrieves & manipulates data to be stored or rendered in view.<p>
  </li>  
    
  <li>
  <p>It should be one to one relation so every ViewModel has only one view and vice versa.<p>
  </li>
  
  <li>
  <p>ViewModel can has subviews which are managed by the activity like dialogs or flow fragments of single operation but it's better to have ViewModel for each View.<p>
  </li> 
  
  <li>
  <p>ViewModel lifecycle should be the same as its view.<p>
  </li>  
    
  </ul>
  </li>
  
  <li>
  <h3>Repository (Data Access Layer)</h3>
   <ul>  
    
  <li>
  <p>It's a layer which provides a clean API for accessing local & remote data.<p>
  </li>
  
  <li>
  <p>It's Centeral Data Manager which manages RestApi requests, local data retrieval & storing.<p>
  </li>
   
  <li>
  <p>ViewModel uses it through calling its methods.<p>
  </li>  
    
  <li>
  <p>ViewModel gets observable variables form repository through methods.<p>
  </li>
  
  <li>
  <p>View gets data updates through observing repository's observable variables.<p>
  </li> 
    
  </ul>
  </li>
          
</ul>

