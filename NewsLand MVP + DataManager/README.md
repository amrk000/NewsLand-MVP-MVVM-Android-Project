<h1><a href="">NewsLand Android Project</a> > MVP + DataManager Version</h1>
<div align="center">
<img src="https://user-images.githubusercontent.com/63168118/158036579-8c62b8f8-7f84-483d-ab6b-da1e4072bd35.png" />
</div>

</br>
<h1 align="center">Model View Presenter (Extended) Architectural Pattern</h1>

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
    
  </ul>
  </li>
          
  <li>
  <h3>Presenter (Logic Class)</h3>
   <ul>  
    
  <li>
  <p>It's the middle layer between View & APP Data.<p>
  </li>
  
  <li>
  <p>View & Presenter are linked together through interfaces.<p>
  </li>
   
  <li>
  <p>It contains all logic which retrieves & manipulates data to be stored or rendered in view.<p>
  </li>  
    
  <li>
  <p>It should be one to one relation so every presenter has only one view and vice versa.<p>
  </li> 
  
  <li>
  <p>Presenter lifecycle should be the same as its view.<p>
  </li>  
    
  </ul>
  </li>
  
  <li>
  <h3>Data Manager (Data Access Layer)</h3>
   <ul>  
    
  <li>
  <p>It's an extension layer which provides a clean API for accessing local & remote data.<p>
  </li>
  
  <li>
  <p>It's Centeral Data Manager which manages RestApi requests, local data retrieval & storing.<p>
  </li>
   
  <li>
  <p>Presenter use it through calling its methods.<p>
  </li>  
    
  <li>
  <p>Presenter get updates from it by observing data changes.<p>
  </li> 
    
  </ul>
  </li>
          
</ul>

