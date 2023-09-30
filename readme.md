# Monitoring a Spring Boot application using Prometheus and Grafana 

## Pre-requisite:
* Please follow the steps mentioned in the below project to set up Prometheus as well as Grafana in Kubernetes.
  * https://github.com/HimanshubhusanRath/k8s-mongodb-grafana

## Steps:
### 1. Deploy the Spring Boot application in kubernetes
* Build and containerize the spring boot application:
  * `mvn clean install`
  * `docker build -t testuser88/springboot-k8s-app:1.0 .`
  * `docker push testuser88/springboot-k8s-app:1.0`
* Below components are defined in `k8s-components.yml` to deploy this spring boot application as well as making it available to Prometheus:
  * Deployment
  * Service
  * ServiceMonitor ---> This will be used by Prometheus to pull metrics from this Spring boot application
* Create these components in Kubernetes.
  * `kubectl apply -f k8s/k8s-components.yml`

* <u>An example of the service monitor to service mapping</u>
![Screenshot 2023-09-29 at 5.20.56 PM.png](..%2F..%2F..%2F..%2F..%2F..%2FDesktop%2FScreenshot%202023-09-29%20at%205.20.56%20PM.png)

### 2. Monitor the Spring Boot application
* Prometheus:
  * Check the spring boot application's ServiceMonitor in Prometheus UI `(Home -> Status -> Targets)`. It should be UP.
* Grafana:
  * Go to Dashboards `(Home -> Dashboards)`
  * Download the 'Spring Boot APM Dashboard' from https://grafana.com/grafana/dashboards/12900-springboot-apm-dashboard/. This is an existing Grafana dashboard for Spring boot applications.
  * Import this dashboard JSON file to Grafana `(Home -> Dashboards -> New -> Import)`
  * Go to this dashboard and check the metrics graphs.
### 3. Alerting in Grafana:
* Setup:
  * For email alerting, we need to mention SMTP configurations in grafana.ini file.
  * However, as Grafana is running in the kubernetes pods, we will not be able to update this file directly as it is immutable by nature.
  * As an alternate approach, we can edit the ConfigMap of this Grafana deployment in Kubernetes and set the SMTP configurations. 
  * Use `kubectl get configmaps` to see all configmaps present in kubernetes.
  * Select the grafana configmap (`main-grafana`)
  * Use `kubectl edit configmap main-grafana` to edit the configmap.
  * Check the below element.
  ```
  Data
  ====
  grafana.ini:
  ```
 
  * Add the below content under this 'grafana.ini' element
  ```
  [smtp]
  enabled=true
  host=smtp.gmail.com:465
  user=a@b@gmail.com --> gmail id
  password=rrerfddeerereerr ----> This is not the mail password. This is App password (discussed below)
  timeout=60
  ```
![Screenshot 2023-09-30 at 7.59.48 PM.png](..%2F..%2F..%2F..%2F..%2F..%2FDesktop%2FScreenshot%202023-09-30%20at%207.59.48%20PM.png)

* <u>NOTE: App Password in Gmail:</u>
  * Log in to gmail
  * Go to `Manage your Google account --> Security`
  * Search 'app password'
  * Enter app name (any name) and click on 'Create'
  * A 16-characters password would be generated.
  * This password needs to be mentioned in the above SMTP configuration.


* Test email:
  * Go to `Home -> Alerting -> Contact Points`
  * Select 'Create Contact Point'
  * Select 'Integration' =  'Email'
  * Mention the recipient address in the 'Address' field.
  * Click 'Test'.
  * If you see 'Test Alert Sent' - it means SMTP is configured correctly.
  * Check if you have received the mail.

* Configure Alerts:
  * Please check this below video.
TODO: VIDEO LINK

* Test alerts:
  * Get the URL for spring boot application:
    * `minikube service springboot-k8s-service`
  * Endpoints:
    * Endpoint without any impact on CPU performance : `[URL]/message`
    * Endpoint impacting the CPU performance : `[URL]/flood`
      * Once you hit this endpoint, infinite number of threads will be created and will be running for infinite time which will impact the CPU performance. 