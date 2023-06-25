(ns kanri.schema
  (:gen-class))

(require '[datomic.api :as d])

(def db-uri "datomic:dev://localhost:4334/kanri") 
(d/create-database db-uri)
(def conn (d/connect db-uri))

(def user
  [{:db/ident :user/name
    :db/valueType :db.type/string
    :db/cardinality :db.cardinality/one
    :db/unique :db.unique/value}
   {:db/ident :user/passwordHash
    :db/valueType :db.type/string
    :db/cardinality :db.cardinality/one
    :db/noHistory true}])

(def task-status
  [{:db/ident :taskStatus/priority}
   {:db/ident :taskStatus/open}
   {:db/ident :taskStatus/suspendedUntil}
   {:db/ident :taskStatus/backlog}
   {:db/ident :taskStatus/done}])
(def task-status-default :taskStatus/open)

(def task
  ; TODO: ensure references a user
  [{:db/ident :task/createdBy
    :db/valueType :db.type/ref
    :db/cardinality :db.cardinality/one}
   {:db/ident :task/createdAt
    :db/valueType :db.type/instant
    :db/cardinality :db.cardinality/one}
   {:db/ident :task/state
    :db/valueType :db.type/ref
    :db/cardinality :db.cardinality/one}
   {:db/ident :task/suspendedUntil
    :db/valueType :db.type/instant
    :db/cardinality :db.cardinality/one}
   {:db/ident :task/project
    :db/valueType :db.type/string
    :db/cardinality :db.cardinality/one}
   {:db/ident :task/description
    :db/valueType :db.type/string
    :db/cardinality :db.cardinality/one}
   {:db/ident :task/comment
    :db/valueType :db.type/string
    :db/cardinality :db.cardinality/one}
   {:db/ident :task/responsible
    :db/valueType :db.type/string
    :db/cardinality :db.cardinality/one}])

(def task-status-log 
  [{:db/ident :taskStatusLog/task
    :db/valueType :db.type/ref
    :db/cardinality :db.cardinality/one}
   {:db/ident :taskStatusLog/createdAt
    :db/valueType :db.type/instant
    :db/cardinality :db.cardinality/one}
   {:db/ident :taskStatusLog/status
    :db/valueType :db.type/ref
    :db/cardinality :db.cardinality/one}])

; TODO: entity specs
(def schema (concat user task-status task task-status-log))

@(d/transact conn schema)