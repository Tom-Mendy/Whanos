# if jenkins cant connect to kubernetes

## in master kubernetes server

```bash
kubectl create serviceaccount jenkins
```

```bash
cat <<EOF | kubectl create -f -
apiVersion: rbac.authorization.k8s.io/v1
kind: ClusterRoleBinding
metadata:
 name: jenkins-integration
 labels:
   k8s-app: jenkins-image-builder
roleRef:
  apiGroup: rbac.authorization.k8s.io
  kind: ClusterRole
  name: cluster-admin
subjects:
- kind: ServiceAccount
  name: jenkins
  namespace: default
EOF
```

```bash
kubectl get secrets $(kubectl get serviceaccounts jenkins -o jsonpath='{.secrets[].name}') -o jsonpath='{.data.token}' | base64 -d
```

```bash
cat <<EOF | kubectl create -f -
apiVersion: v1
kind: Secret
type: kubernetes.io/service-account-token
metadata:
 name: jenkins
 annotations:
   kubernetes.io/service-account.name: jenkins
EOF
```

```bash
kubectl get secrets jenkins -o jsonpath='{.data.token}' | base64 -d
```

copy the token

## env

paste

to `KUBE_TOKEN=<token>`

make sure `KUBE_SERVER=<ip>` equal the right server

## apply change

run

```bash
./run.sh
```
