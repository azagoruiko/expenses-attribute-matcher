job "attribute-matcher" {
  datacenters = ["home"]
  type        = "service"

  group "attribute-matcher-group" {
    count = 2

    constraint {
      operator = "distinct_hosts"
      value = "true"
    }

    restart {
      attempts = 10
      interval = "5m"
      delay    = "25s"
      mode     = "delay"
    }

    task "attribute-matcher-task" {
      driver = "docker"
      template {
        data = <<EOH
MYSQL_URL="{{ key "jdbc.url" }}"
MYSQL_DRIVER="{{ key "jdbc.driver" }}"
MYSQL_USER="{{ key "jdbc.user" }}"
MYSQL_PASSWORD="{{ key "jdbc.password" }}"
EOH
        destination = "secrets.env"
        env = true
      }
      config {
        image = "127.0.0.1:9999/docker/attribute-matching:0.0.8"
        
        port_map {
          web = 8080
        }
      }

      resources {
        cpu    = 400
        memory = 1024

        network {
          port "web" {}
        }
      }

      service {
        name = "attribute-matcher"
        port = "web"
        tags = ["urlprefix-/matcher strip=/matcher", "urlprefix-/"]

        check {
          type     = "http"
          path     = "/actuator/health"
          interval = "10s"
          timeout  = "10s"
        }
      }
    }
  }
}
