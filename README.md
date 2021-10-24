# Template Project w/ gRPC Microservices and Kubernetes

gRPC Microservices (Spring Boot + Java) with Kubernetes and Docker on AWS EKS & K3s (Lightweight Kubernetes)

This is a complete template project for demo purposes, using the latest technologies with deployment tools.

It shows how to

1. use lightweight communication protocol to communicate between micro-services like [gRPC](https://grpc.io) using
   Spring Boot and Java;
2. package a Spring Boot application into a Docker image, using [Skaffold](https://skaffold.dev/)
   and [Google Jib](https://github.com/GoogleContainerTools/jib);
3. Deploy it into your Kubernetes cluster.

# Requirements

You will need the following in your system:

* [Skaffold](https://skaffold.dev) 1.33.1 or better
* [Kubectl](https://kubernetes.io/docs/tasks/tools/install-kubectl/) 1.22.0 or better
* Docker registry (for me i use Github https://ghcr.io)
* a local Kubernetes cluster
  like [Docker for Desktop CE](https://hub.docker.com/editions/community/docker-ce-desktop-windows)
  or [K3s](https://k3s.io/) if you want to develop with your local
  cluster,  [here](https://gist.github.com/mohammedamineboutouil/c6f4868a711a55b1c2e16f13e11a4202) you can find simple
  script to run k3s cluster multi-nodes with [istio](https://istio.io/) using [Multipass](https://multipass.run/).

(Note: if you want to work directly with your remote AWS cluster, having a local cluster is not strictly needed but
strongly advised!)

# Lifecycle

### Build your code push to registry

This builds and pushes a container image for your application to a container registry. *If you encounter authentication
issues,
see [Authentication Methods](https://github.com/GoogleContainerTools/jib/tree/master/jib-gradle-plugin#authentication-methods)
.*

```
./gradlew jib
```

(Note: please put your registry info
in [gradle.properties](https://github.com/mohammedamineboutouil/spring-grpc-microservices-kubernetes/blob/main/gradle.properties)  "
registryName", "registryUser", "registryPassword" :))

### Run and Deploy

First you must configure your cluster to use Container Registry if you are using a private
registry *[Good Article](https://itnext.io/build-ship-github-container-registry-kubernetes-aa06029b3f21) As example (
Github Registry):

```
kubectl create secret docker-registry my-ghcr \
        --docker-server=ghcr.io \
        --docker-username=dummy \
        --docker-password=dummyToken \
        --docker-email=dummy@dummy.com \
        --namespace=default
```

You code in your IDEs and deploy/run explicitly by typing:

```
skaffold run --port-forward # to test call `curl http://127.0.0.1:5000/grpc-client/users`
```

(Note: Istio gateway are exposed to port 80 with /api prefix your url it will look like this http:
//{YourClusterIp}/api/grpc-client/users )

Happy coding!

### Delete your deployment

```
skaffold delete
```