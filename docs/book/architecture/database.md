---
name: Datenbank
---


# Übersicht

## Tech-Stack

## Diagramm
Blablabla, hier ist ein Diagramm

:::mermaid
classDiagram
    Animal <|-- Duck
    Animal <|-- Fish
    Animal <|-- Zebra
    Animal : +int age
    Animal : +String gender
    Animal: +isMammal()
    Animal: +mate()
    class Duck{
      +String beakColor
      +swim()
      +quack()
    }
    class Fish{
      -int sizeInFeet
      -canEat()
    }
    class Zebra{
      +bool is_wild
      +run()
    }
:::

# Flyway
Blablabla, flyway stuff